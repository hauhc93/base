package jp.co.skylark.digital_gusto.call_display

import com.github.michaelbull.result.Result
import jp.co.skylark.digital_gusto.DigitalGustoListenerRegistration
import jp.co.skylark.digital_gusto.call_display.data.Call

internal object DigitalGustoCallDisplayDummyImpl : DigitalGustoCallDisplay {

    override suspend fun call(shopId: Int, tableNo: Int, serviceId: Int): Result<Unit, Throwable> =
        com.github.michaelbull.result.runCatching {
            TODO("Not yet implemented")
        }

    override suspend fun apply(
        shopId: Int,
        tableNo: Int,
        serviceIds: List<Int>
    ): Result<Unit, Throwable> =
        com.github.michaelbull.result.runCatching {
            TODO("Not yet implemented")
        }

    override fun addListener(
        shopId: Int,
        listener: (calls: List<Call>, error: Throwable?) -> Unit
    ): DigitalGustoListenerRegistration {
        TODO("Not yet implemented")
    }
}