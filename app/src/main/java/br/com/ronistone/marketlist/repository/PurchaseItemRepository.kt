package br.com.ronistone.marketlist.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.Database
import br.com.ronistone.marketlist.dao.PurchaseItemDao
import br.com.ronistone.marketlist.dao.model.Converters
import br.com.ronistone.marketlist.data.PurchaseItemApi
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class PurchaseItemRepository private constructor(
    private val purchaseItemApi: PurchaseItemApi,
    private val purchaseItemDao: PurchaseItemDao,
    private val productRepository: ProductRepository,

) {

    companion object {
        private var instance: PurchaseItemRepository? = null

        fun getInstance(context: Context): PurchaseItemRepository {
            if (instance == null) {
                synchronized(this){
                    if(instance == null) {
                        instance = PurchaseItemRepository(
                            PurchaseItemApi.create(),
                            Database.getDatabase(context)!!.purchaseItemDao(),
                            ProductRepository.getInstance(context)
                        )
                    }
                }
            }
            return instance!!
        }
    }

    suspend fun fetchItem(purchaseId: Int, itemId: Int, result: MutableLiveData<PurchaseItem?>): Boolean {
        val item = purchaseItemDao.getItem(purchaseId, itemId)

        item?.let {
            val purchaseResult = Converters.purchaseItemWithDependenciesToPurchaseItem(item)

            result.postValue(purchaseResult)
        }


        val apiResponse = purchaseItemApi.getItem(purchaseId, itemId)
        withContext(Dispatchers.Main) {
            if(apiResponse.isSuccessful) {
                result.postValue(apiResponse.body())
            } else {
                throw RuntimeException("Fail to fetch")
            }
        }

        return true
    }

    suspend fun updateItem(item: PurchaseItem, purchaseId: Int): Purchase? {
        val response = purchaseItemApi.updateItem(purchaseId, item.id!!, item)
        response.body()?.let {
            updatePurchase(it)
            return response.body()
        }
        return null
    }

    suspend fun removeItem(item: PurchaseItem, purchaseId: Int): Boolean {
        purchaseItemApi.removeItem(purchaseId, item.id!!)
        purchaseItemDao.deleteItem(purchaseId, item.id)
        return true
    }

    suspend fun addItem(item: PurchaseItem, purchaseId: Int): Purchase? {
        val response = purchaseItemApi.addItem(purchaseId, item)
        response.body()?.let {
            updatePurchase(it)
            return response.body()
        }
        return null
    }

    suspend fun updatePurchase(purchase: Purchase) {
        purchaseItemDao.cleanPurchaseItems(purchase.id!!)
        productRepository.cleanByPurchase(purchase.id)
        purchase.items?.forEach {
            productRepository.create(it.product, purchase.id)
            val item = Converters.purchaseItemToPurchaseItemDao(it, purchase.id)
            purchaseItemDao.insert(item)
        }
    }
}
