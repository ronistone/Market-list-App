package br.com.ronistone.marketlist.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.Database
import br.com.ronistone.marketlist.dao.ProductDao
import br.com.ronistone.marketlist.dao.model.Converters
import br.com.ronistone.marketlist.data.ProductApi
import br.com.ronistone.marketlist.model.Product
import br.com.ronistone.marketlist.model.ProductInstance
import br.com.ronistone.marketlist.model.PurchaseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository private constructor(
    private val productApi: ProductApi,
    private val productDao: ProductDao,
) {

    companion object {
        private var instance: ProductRepository? = null

        fun getInstance(context: Context): ProductRepository {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ProductRepository(
                            ProductApi.create(),
                            Database.getDatabase(context)!!.productDao()
                        )
                    }
                }
            }
            return instance!!
        }
    }

    suspend fun getByName(name: String, result: MutableLiveData<List<Product>>): Boolean {
        val response = productApi.getProductsByName(name)

        withContext(Dispatchers.Main) {
            result.postValue(response.body())
        }
        return true
    }

    suspend fun getByEan(ean: String, result: MutableLiveData<PurchaseItem?>): Boolean {
        val response = productApi.getProductByEan(ean)

        withContext(Dispatchers.Main) {
            result.postValue(PurchaseItem(
                productInstance = ProductInstance(
                    product = response.body()!!,
                )
            ))
        }
        return true
    }

    suspend fun create(product: Product, purchaseId: Int): Boolean {
        productDao.insert(Converters.productModelToProductDao(product))
        return true
    }

    suspend fun cleanByPurchase(purchaseId: Int) {
        productDao.cleanTable(purchaseId)
    }
}
