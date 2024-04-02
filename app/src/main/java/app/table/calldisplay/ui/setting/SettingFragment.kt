package app.table.calldisplay.ui.setting

import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.table.calldisplay.R
import app.table.calldisplay.base.ui.BaseFragment
import app.table.calldisplay.databinding.FragmentSettingBinding
import app.table.calldisplay.extensions.onClick
import app.table.calldisplay.ui.setting.dialog.setting.SettingDialogFragment
import app.table.calldisplay.ui.setting.dialog.shopsetting.ShopIdSettingDialogFragment
import app.table.calldisplay.util.AppConstant
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
    }
    private var binding: FragmentSettingBinding? = null
    internal var onDismissDialogListener: () -> Unit = {}

    private var identificationId = AppConstant.APPLICATIONID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.data = viewModel
        }
        Firebase.analytics.logEvent("sys_log", bundleOf("message" to "SettingFragment.onCreateView Identification Id: $identificationId"))
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Firebase.analytics.logEvent("sys_log", bundleOf("message" to "SettingFragment.onDestroyView Identification Id: $identificationId"))
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initListener() {
        binding?.run {
            cardViewSound.onClick {
                openSoundSettingDialog()
            }

            cardViewRequest.onClick {
                viewModel.setRequestItems()
                SettingDialogFragment.newInstance(
                    context?.getString(R.string.textViewRequestSettingsTitle) ?: "",
                    viewModel.getRequestItems()
                ).show(childFragmentManager, SettingDialogFragment::class.java.name)
            }

            cardViewTime.onClick {
                viewModel.setRequestItems()
                SettingDialogFragment.newInstance(
                    context?.getString(R.string.textViewTimeSettingsTitle) ?: "",
                    viewModel.getTimer()
                ).show(childFragmentManager, SettingDialogFragment::class.java.name)
            }

            imgBack.onClick {
                findNavController().setGraph(R.navigation.nav_main_graph)
            }

            cardViewShopId.onClick {
                ShopIdSettingDialogFragment.newInstance()
                    .show(childFragmentManager, ShopIdSettingDialogFragment::class.java.name)
            }
        }
    }

    private fun initView() {
        viewModel.setRingtoneItems(getRingtoneList(), getCurrentRingtone())
        binding?.run {
            defaultView()
            onDismissDialogListener = {
                defaultView()
            }

            lifecycleScope.launch {
                viewModel.getShopId().collect {
                    tvShopId.text = it
                }
            }
        }
    }

    private fun defaultView() {
        binding?.run {
            setTimer()
            tvContent.text = requireContext().getString(
                R.string.textViewRequestNumber,
                viewModel.getSettingInfo().request.toString()
            )
            tvContentSound.text = getCurrentRingtone()
        }
    }

    private fun setTimer() {
        binding?.run {
            if (viewModel.getSettingInfo().time == 0) {
                tvContentTime.text = context?.getString(R.string.textViewDisableAutoDelete)
            } else {
                tvContentTime.text =
                    context?.getString(R.string.textViewTimeNumber, viewModel.getSettingInfo().time)
            }
        }
    }

    private fun openSoundSettingDialog() {
        SettingDialogFragment.newInstance(
            context?.getString(R.string.textViewRequestSettingsTitle) ?: "",
            viewModel.getRingtoneItems()
        ).show(childFragmentManager, SettingDialogFragment::class.java.name)
    }

    private fun getCurrentRingtone(): String {
        val defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
            activity?.applicationContext,
            RingtoneManager.TYPE_NOTIFICATION
        )
        return if (viewModel.getSettingInfo().ringtoneName.isNullOrEmpty()) {
            RingtoneManager.getRingtone(activity, defaultRingtoneUri).getTitle(requireContext())
        } else {
            viewModel.getSettingInfo().ringtoneName.toString()
        }

    }

    private fun getRingtoneList(): Map<String, String> {
        val manager = RingtoneManager(activity)
        manager.setType(RingtoneManager.TYPE_NOTIFICATION)
        val cursor = manager.cursor
        val list: MutableMap<String, String> = HashMap()
        while (cursor.moveToNext()) {
            val notificationTitle: String = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val notificationUri: String = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                .toString() + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
            list[notificationTitle] = notificationUri
        }
        return list
    }
}
