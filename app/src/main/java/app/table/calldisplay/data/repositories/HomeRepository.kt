package app.table.calldisplay.data.repositories

import app.table.calldisplay.ui.home.model.RequestOrder
import app.table.calldisplay.util.LogUtils
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import jp.co.skylark.digital_gusto.DigitalGustoListenerRegistration
import jp.co.skylark.digital_gusto.call_display.DigitalGustoCallDisplay
import jp.co.skylark.digital_gusto.call_display.data.Call

@Singleton
class HomeRepository @Inject constructor(
    private val digitalGustoCallDisplay: DigitalGustoCallDisplay,
    private val localRepository: LocalRepository
) {
    private val shopIdInt: Int
        get() = localRepository.getShopId().toIntOrNull() ?: 0

    suspend fun applyOrder(
        requestOrder: RequestOrder, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}
    ) {
        digitalGustoCallDisplay.apply(
            requestOrder.shopId,
            requestOrder.tableNo,
            requestOrder.services.map { it.id }
        ).onSuccess {
            onSuccess.invoke()
            LogUtils.i("FireStore: Apply Success data:${requestOrder}")
        }.onFailure {
            onFailure.invoke()
            LogUtils.i("FireStore: Apply Failure data:${requestOrder}")
        }
    }

    fun getDigitalGustoCallDisplay(listener: (calls: List<Call>, error: Throwable?) -> Unit): DigitalGustoListenerRegistration =
        digitalGustoCallDisplay.addListener(shopIdInt, listener)

    fun getLimitRequestNewList(): Int = localRepository.getSettingInfo().request

    fun getRingToneURI(): String? = localRepository.getSettingInfo().url

    fun getTimeMillisAutoDelete(): Long =
        TimeUnit.MINUTES.toMillis(localRepository.getSettingInfo().time?.toLong() ?: 0L)
}
