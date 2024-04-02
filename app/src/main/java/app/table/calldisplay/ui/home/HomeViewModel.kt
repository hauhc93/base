package app.table.calldisplay.ui.home

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import app.table.calldisplay.base.ui.BaseViewModel
import app.table.calldisplay.data.repositories.HomeRepository
import app.table.calldisplay.ui.home.model.RequestOrder
import app.table.calldisplay.ui.home.model.Service
import app.table.calldisplay.util.LogUtils
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import java.util.Timer
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import jp.co.skylark.digital_gusto.DigitalGustoListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

    private val limitRequestOrdersDisplay: Int =
        homeRepository.getLimitRequestNewList()
    private val listenerRegistration: DigitalGustoListenerRegistration =
        initListenerRequestOrderFireStore()

    private val _isRequestOrderEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _requestOrders: MutableStateFlow<List<RequestOrder>> = MutableStateFlow(emptyList())
    private val _requestOrdersOld: MutableStateFlow<List<RequestOrder>> =
        MutableStateFlow(emptyList())
    private var oldUpdateTimeSeconds: Long = 0L
    private var timerAutoDelete: Timer? = null
    private val dateStartScanToDelete: Date by lazy {
        Date().apply {
            time += homeRepository.getTimeMillisAutoDelete()
        }
    }

    internal var isTablet: Boolean = false

    val isRequestOrderEmpty: StateFlow<Boolean> = _isRequestOrderEmpty
    val isRequestOrderOldEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val requestOrders: StateFlow<List<RequestOrder>> = _requestOrders
    val requestOrdersOld: StateFlow<List<RequestOrder>> = _requestOrdersOld
    val newUpdateTimeObserver: MutableStateFlow<Long> = MutableStateFlow(0)
    var connection: MutableStateFlow<Boolean> = MutableStateFlow(true)

    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
        timerAutoDelete?.cancel()
    }

    // region Function

    private fun initListenerRequestOrderFireStore(): DigitalGustoListenerRegistration =
        homeRepository.getDigitalGustoCallDisplay { calls, error ->
            if (error != null) {
                LogUtils.e("FireStore: addListener Failure error:${error}")
            }
            handleGetListRequestSuccess(calls.map { call ->
                Firebase.analytics.logEvent(
                    "event_log",
                    bundleOf("message" to "Get list request success at shopId: ${call.shopId} with tableNo: ${call.tableNo}")
                )
                RequestOrder(
                    call.shopId,
                    call.tableNo,
                    call.services.map { service -> Service.fromServiceDigitalGusto(service) }
                )
            })
        }

    private fun handleGetListRequestSuccess(list: List<RequestOrder>) {
        handleArrangeListTable(list)
        _isRequestOrderEmpty.value = list.isEmpty()
        notifyNewUpdateTimeSeconds(list)
    }

    private fun handleArrangeListTable(list: List<RequestOrder>) {
        if (list.size > limitRequestOrdersDisplay && isTablet) {
            _requestOrders.value = list.subList(0, limitRequestOrdersDisplay)
            _requestOrdersOld.value = list.subList(
                limitRequestOrdersDisplay,
                list.size
            )
            isRequestOrderOldEmpty.value = false
        } else {
            isRequestOrderOldEmpty.value = true
            _requestOrders.value = list
            _requestOrdersOld.value = emptyList()
        }
    }

    private fun notifyNewUpdateTimeSeconds(list: List<RequestOrder>) {
        val newUpdateTimeSeconds =
            list.flatMap { it.services }.maxOfOrNull { it.updateTimeSeconds } ?: 0L
        newUpdateTimeObserver.value = newUpdateTimeSeconds
    }

    private fun getTimerScanDelete(): Timer {
        LogUtils.d("startAt: $dateStartScanToDelete")
        Firebase.analytics.logEvent(
            "event_log",
            bundleOf("message" to "Date starts scan delete:  $dateStartScanToDelete")
        )
        val timeMillisIntervalScan: Long = homeRepository.getTimeMillisAutoDelete()
        resumeDateStartScanToDelete()
        return kotlin.concurrent.timer(
            "ScanToDeleteRequestExpired",
            startAt = dateStartScanToDelete,
            period = timeMillisIntervalScan
        ) {
            val currentTimeMillis: Long = System.currentTimeMillis()
            LogUtils.d("ScanDelete: $currentTimeMillis.")
            mutableListOf<RequestOrder>().apply {
                addAll(_requestOrders.value)
                addAll(_requestOrdersOld.value)
            }.forEach {
                if (currentTimeMillis - TimeUnit.SECONDS.toMillis(it.updateTimeSeconds) >= timeMillisIntervalScan) {
                    LogUtils.d("Auto Delete: $it.")
                    applyOderRequestApi(it)
                }
            }
            dateStartScanToDelete.time = currentTimeMillis + timeMillisIntervalScan
        }
    }

    private fun resumeDateStartScanToDelete() {
        LogUtils.d("Before$dateStartScanToDelete")
        while (dateStartScanToDelete.time < System.currentTimeMillis()) {
            dateStartScanToDelete.time += homeRepository.getTimeMillisAutoDelete()
        }
        LogUtils.d("After$dateStartScanToDelete")
    }

    internal fun applyOderRequestApi(requestOrder: RequestOrder) {
        viewModelScope.launch {
            homeRepository.applyOrder(requestOrder)
        }
    }

    internal fun getRingToneUri(): Uri = Uri.parse(homeRepository.getRingToneURI())

    internal fun isHasBeenNewRequest(newTimeSeconds: Long): Boolean =
        newTimeSeconds > oldUpdateTimeSeconds

    internal fun updateOldUpdateTimeSeconds(newTimeSeconds: Long) {
        oldUpdateTimeSeconds = newTimeSeconds
    }

    internal fun configTimerScanToAutoDelete() {
        timerAutoDelete?.cancel()
        if (homeRepository.getTimeMillisAutoDelete() > 0) {
            timerAutoDelete = getTimerScanDelete()
        }
    }

    internal fun cancelScanDelete() {
        timerAutoDelete?.cancel()
        timerAutoDelete = null
    }

    // endregion Function
}
