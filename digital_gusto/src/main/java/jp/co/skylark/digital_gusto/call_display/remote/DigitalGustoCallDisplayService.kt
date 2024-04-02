package jp.co.skylark.digital_gusto.call_display.remote

import jp.co.skylark.digital_gusto.call_display.remote.data.Service
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

internal interface DigitalGustoCallDisplayService {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/shops/{shopId}/tables/{tableNo}")
    suspend fun call(
        @Path("shopId") shopId: Int,
        @Path("tableNo") tableNo: Int,
        @Body service: Service
    ): Response<Unit>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("/shops/{shopId}/tables/{tableNo}/apply")
    suspend fun apply(
        @Path("shopId") shopId: Int,
        @Path("tableNo") tableNo: Int,
        @Body services: List<Service>
    ): Response<Unit>
}
