package br.com.ronistone.marketlist.data

import br.com.ronistone.marketlist.Constants
import br.com.ronistone.marketlist.model.Market
import br.com.ronistone.marketlist.model.Purchase
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MarketApi {

    @GET("v1/market/")
    suspend fun listMarkets(): Response<List<Market>>

    @GET("v1/market/{id}")
    suspend fun getMarket(@Path("id") id: Int): Response<Market>

    @POST("v1/market/")
    suspend fun createMarket(@Body market: Market): Response<Market>


    companion object {

        fun create(): MarketApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(JsonConverterFactory.create())
                .build()

            return retrofit.create(MarketApi::class.java)
        }

    }
}
