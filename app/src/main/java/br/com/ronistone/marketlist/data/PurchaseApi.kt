package br.com.ronistone.marketlist.data

import br.com.ronistone.marketlist.Constants
import br.com.ronistone.marketlist.model.Purchase
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchaseApi {

    @GET("v1/purchase/")
    suspend fun listPurchases(): Response<List<Purchase>>

    @GET("v1/purchase/{id}")
    suspend fun getPurchase(@Path("id") id: Int): Response<Purchase>

    @POST("v1/purchase/")
    suspend fun createPurchase(@Body purchase: Purchase): Response<Purchase>

    companion object {

        fun create(): PurchaseApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(JsonConverterFactory.create())
                .build()

            return retrofit.create(PurchaseApi::class.java)
        }

    }
}
