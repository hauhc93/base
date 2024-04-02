package jp.co.skylark.digital_gusto.call_display

import com.github.michaelbull.result.Result
import jp.co.skylark.digital_gusto.DigitalGustoApp
import jp.co.skylark.digital_gusto.DigitalGustoAppImpl
import jp.co.skylark.digital_gusto.DigitalGustoListenerRegistration
import jp.co.skylark.digital_gusto.call_display.data.Call

interface DigitalGustoCallDisplay {

    suspend fun call(shopId: Int, tableNo: Int, serviceId: Int): Result<Unit, Throwable>
    suspend fun apply(shopId: Int, tableNo: Int, serviceIds: List<Int>): Result<Unit, Throwable>
    fun addListener(
        shopId: Int,
        listener: (calls: List<Call>, error: Throwable?) -> Unit
    ): DigitalGustoListenerRegistration

    companion object {

        fun getInstance(app: DigitalGustoApp): DigitalGustoCallDisplay {
            if (app is DigitalGustoAppImpl) {
                return DigitalGustoCallDisplayImpl(app)
            }

            return DigitalGustoCallDisplayDummyImpl
        }
    }
}