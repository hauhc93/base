package app.table.calldisplay.ui.home

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.table.calldisplay.R
import app.table.calldisplay.base.ui.BaseFragment
import app.table.calldisplay.databinding.FragmentHomeBinding
import app.table.calldisplay.ui.home.deletedialog.ConfirmDeleteRequestDialog
import app.table.calldisplay.ui.home.model.RequestOrder
import app.table.calldisplay.util.AppConstant
import app.table.calldisplay.util.LogUtils
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.net.InetAddress
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel by viewModels<HomeViewModel>()
    private var binding: FragmentHomeBinding? = null
    private val requestListAdapter: RequestListAdapter by lazy {
        RequestListAdapter()
    }
    private val requestOldListAdapter: RequestOldListAdapter by lazy {
        RequestOldListAdapter()
    }
    private var mediaPlayer: MediaPlayer? = null

    private var identificationId: String = AppConstant.APPLICATIONID

    // region Override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.analytics.setUserId(AppConstant.APPLICATIONID)
        Firebase.crashlytics.setUserId(AppConstant.APPLICATIONID)

        Firebase.analytics.logEvent(
            "sys_log",
            bundleOf("message" to "HomeFragment.onCreate Identification Id: $identificationId")
        )
        viewModel.isTablet = resources.getBoolean(R.bool.isTablet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding?.also {
            it.data = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListener()
        initFlowObserver()
        handleNetworkChange(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.configTimerScanToAutoDelete()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.release()
        viewModel.cancelScanDelete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        binding = null
    }
    // endregion

    private fun initViews() {
        binding?.run {
            rvListNewRequest?.adapter = requestListAdapter
            rvListSooOldRequest.adapter = requestOldListAdapter
        }
    }

    private fun initListener() {
        requestListAdapter.onItemLongClicked = {
            initAndShowDialogConfirmDelete(it)
        }

        binding?.let {
            it.layoutHeader.imgSetting.setOnClickListener {
                findNavController().navigate(R.id.actionHomeFragmentToSettingFragment)
            }
            it.flexBoxNewRequest?.onItemLongClicked = { itemData ->
                initAndShowDialogConfirmDelete(itemData)
            }
            it.flexBoxNewRequest?.onDragListener = { itemData ->
                postApplyOrderToServer(itemData)
                Firebase.analytics.logEvent(
                    "event_log",
                    bundleOf("message" to "delete table by swipe: ${itemData.tableNo} at shopId: ${itemData.shopId}")
                )
            }
        }
        handleSwipeToDeleteListNewRequest()
    }

    private fun initFlowObserver() {
        lifecycleScope.launchWhenResumed {
            LogUtils.d("launchWhenStarted")
            launch {
                viewModel.requestOrders.collect {
                    requestListAdapter.submitList(it)
                    binding?.flexBoxNewRequest?.submitList(it)
                }
            }
            launch {
                viewModel.requestOrdersOld.collect {
                    requestOldListAdapter.submitList(it)
                }
            }
            launch {
                viewModel.newUpdateTimeObserver.collect {
                    if (viewModel.isHasBeenNewRequest(it)) {
                        handlePlaySoundInDuration()
                        viewModel.updateOldUpdateTimeSeconds(it)
                    }
                }
            }
        }
    }

    private fun postApplyOrderToServer(requestOrder: RequestOrder) {
        viewModel.applyOderRequestApi(requestOrder)
    }

    private fun handleSwipeToDeleteListNewRequest() {
        binding?.rvListNewRequest?.also { rv ->
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val requestOrder: RequestOrder =
                        requestListAdapter.currentList[viewHolder.layoutPosition]
                    val newList = requestListAdapter.currentList.filterNot { it == requestOrder }
                    Firebase.analytics.logEvent(
                        "event_log",
                        bundleOf("message" to "delete table by swipe: ${requestOrder.tableNo} at shopId: ${requestOrder.shopId}")
                    )
                    requestListAdapter.submitList(newList)
                    postApplyOrderToServer(requestOrder)
                }
            })
            itemTouchHelper.attachToRecyclerView(rv)
        }
    }

    private fun handlePlaySoundInDuration() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, viewModel.getRingToneUri())
        mediaPlayer?.start()
    }

    private fun initAndShowDialogConfirmDelete(requestToDelete: RequestOrder) {
        ConfirmDeleteRequestDialog().apply {
            onConfirmClicked = {
                postApplyOrderToServer(requestToDelete)
                Firebase.analytics.logEvent(
                    "event_log",
                    bundleOf("message" to "delete table by onLongClick: ${requestToDelete.tableNo} at shopId: ${requestToDelete.shopId}")
                )
            }
        }.show(parentFragmentManager, null)
    }

    private fun handleNetworkChange(context: Context) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                viewModel.connection.value = isInternetAvailable()
                Firebase.analytics.logEvent(
                    "error_log",
                    bundleOf("message" to "connect network access Identification Id: $identificationId")
                )
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                viewModel.connection.value = false
                Firebase.analytics.logEvent(
                    "error_log",
                    bundleOf("message" to "disconnect network access Identification Id: $identificationId")
                )
            }
        }

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        viewModel.connection.value = connectivityManager.activeNetwork != null
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr = InetAddress.getByName("google.com")
            //You can replace it with your name
            ipAddr.hostAddress != ""
        } catch (e: Exception) {
            false
        }
    }
}
