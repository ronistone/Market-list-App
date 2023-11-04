package br.com.ronistone.marketlist.data

import br.com.ronistone.marketlist.Constants
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PurchaseItemApi {
    @POST("v1/purchase/{purchaseId}/item/")
    suspend fun addItem(@Path("purchaseId") purchaseId: Int, @Body item: PurchaseItem): Response<Purchase>

    @DELETE("v1/purchase/{purchaseId}/item/{itemId}")
    suspend fun removeItem(@Path("purchaseId") purchaseId: Int, @Path("itemId") itemId: Int): Response<Purchase>

    @GET("v1/purchase/{purchaseId}/item/{itemId}")
    suspend fun getItem(@Path("purchaseId") purchaseId: Int, @Path("itemId") itemId: Int): Response<PurchaseItem>

    @PUT("v1/purchase/{purchaseId}/item/{itemId}")
    suspend fun updateItem(@Path("purchaseId") purchaseId: Int, @Path("itemId") itemId: Int, @Body item: PurchaseItem): Response<Purchase>

    companion object {

        fun create(): PurchaseItemApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(JsonConverterFactory.create())
                .build()

            return retrofit.create(PurchaseItemApi::class.java)
        }

    }
}
