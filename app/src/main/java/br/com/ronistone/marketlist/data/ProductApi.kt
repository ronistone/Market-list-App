package br.com.ronistone.marketlist.data

import br.com.ronistone.marketlist.Constants
import br.com.ronistone.marketlist.model.Product
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("/v1/product/name/{name}")
    suspend fun getProductsByName(@Path("name") name: String): Response<List<Product>>

    @GET("/v1/product/ean/{ean}")
    suspend fun getProductByEan(@Path("ean") ean: String): Response<Product>

    companion object {

        fun create(): ProductApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(JsonConverterFactory.create())
                .build()

            return retrofit.create(ProductApi::class.java)
        }

    }
}
