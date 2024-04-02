package jp.co.skylark.digital_gusto.call_display

import com.github.michaelbull.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.co.skylark.digital_gusto.BuildConfig
import jp.co.skylark.digital_gusto.DigitalGustoAppImpl
import jp.co.skylark.digital_gusto.DigitalGustoListenerRegistration
import jp.co.skylark.digital_gusto.call_display.data.Call
import jp.co.skylark.digital_gusto.call_display.data.Service
import jp.co.skylark.digital_gusto.call_display.remote.DigitalGustoCallDisplayService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal class DigitalGustoCallDisplayImpl(private val app: DigitalGustoAppImpl) :
    DigitalGustoCallDisplay {

    private val firestore = FirebaseFirestore.getInstance(app.firebaseApp).apply {
        firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
    }

    private val moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()

    private val okHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor {
                Timber.i(it)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            addInterceptor(loggingInterceptor)
        }
    }.build()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl(app.baseUrl)
        addConverterFactory(MoshiConverterFactory.create(moshi))
        client(okHttpClient)
    }.build()

    private val api = retrofit.create(DigitalGustoCallDisplayService::class.java)

    override suspend fun call(shopId: Int, tableNo: Int, serviceId: Int): Result<Unit, Throwable> =
        com.github.michaelbull.result.runCatching {
            api.call(
                shopId,
                tableNo,
                jp.co.skylark.digital_gusto.call_display.remote.data.Service(serviceId)
            )
        }

    override suspend fun apply(
        shopId: Int,
        tableNo: Int,
        serviceIds: List<Int>
    ): Result<Unit, Throwable> =
        com.github.michaelbull.result.runCatching {
            api.apply(
                shopId,
                tableNo,
                serviceIds.map { jp.co.skylark.digital_gusto.call_display.remote.data.Service(it) })
        }

    private data class CallDocument(
        val shopId: Int,
        val tableNo: Int,
        val serviceId: Int,
        val count: Int,
        val updateTimeSeconds: Long,
        val firstCallTimeSeconds: Long
    )

    override fun addListener(
        shopId: Int,
        listener: (calls: List<Call>, error: Throwable?) -> Unit
    ): DigitalGustoListenerRegistration {
        val docRef = firestore
            .collection(COLLECTION_SHOPS).document(shopId.toString())
            .collection(COLLECTION_CALLS).whereEqualTo(FIELD_DELETE_FLAG, false)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // TODO
                listener.invoke(emptyList(), error)
                return@addSnapshotListener
            }

            val docs = snapshot?.documents ?: emptyList()
            val calls = docs
                .map {
                    CallDocument(
                        shopId = it.get(FIELD_SHOP_ID).toString().toInt(10),
                        tableNo = it.get(FIELD_TABLE_NO).toString().toInt(10),
                        serviceId = it.get(FIELD_SERVICE_ID).toString().toInt(10),
                        count = it.get(FIELD_COUNT).toString().toInt(10),
                        updateTimeSeconds = it.getTimestamp(FIELD_UPDATE_TIME)?.seconds ?: 0,
                        firstCallTimeSeconds = it.getTimestamp(FIELD_FIRST_CALL_TIME)?.seconds ?: 0
                    )
                }
                .groupBy { it.shopId to it.tableNo }
                .map {
                    val services = it.value
                        .map { a ->
                            Service(
                                a.serviceId,
                                a.count,
                                a.updateTimeSeconds,
                                a.firstCallTimeSeconds
                            )
                        }
                        // TODO sort
                        .sortedByDescending { a -> a.updateTimeSeconds }
                    Call(it.key.first, it.key.second, services)
                }
                // TODO sort
                .sortedBy {
                    it.firstCallTimeSeconds
                }

            listener.invoke(calls, null)
        }

        return object : DigitalGustoListenerRegistration {
            override fun remove() {
                registration.remove()
            }
        }
    }

    companion object {
        private const val COLLECTION_SHOPS = "shops"
        private const val COLLECTION_CALLS = "calls"
        private const val FIELD_DELETE_FLAG = "delete_flag"
        private const val FIELD_UPDATE_TIME = "update_time"
        private const val FIELD_SHOP_ID = "shop_id"
        private const val FIELD_TABLE_NO = "table_no"
        private const val FIELD_SERVICE_ID = "service_id"
        private const val FIELD_COUNT = "count"
        private const val FIELD_FIRST_CALL_TIME = "first_call_time"
    }
}