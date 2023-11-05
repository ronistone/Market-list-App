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
    private val productInstanceRepository: ProductInstanceRepository

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
                            ProductInstanceRepository.getInstance(context)
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

    suspend fun updateItem(item: PurchaseItem): Purchase? {
        val response = purchaseItemApi.updateItem(item.purchase?.id!!, item.id!!, item)
        response.body()?.let {
            updatePurchase(it)
            return response.body()
        }
        return null
    }

    suspend fun removeItem(item: PurchaseItem): Boolean {
        purchaseItemApi.removeItem(item.purchase?.id!!, item.id!!)
        purchaseItemDao.deleteItem(item.purchase.id, item.id)
        return true
    }

    suspend fun addItem(item: PurchaseItem): Purchase? {
        val response = purchaseItemApi.addItem(item.purchase?.id!!, item)
        response.body()?.let {
            updatePurchase(it)
            return response.body()
        }
        return null
    }

    suspend fun updatePurchase(purchase: Purchase) {
        purchaseItemDao.cleanPurchaseItems(purchase.id!!)
        productInstanceRepository.cleanByPurchase(purchase.id)
        purchase.items?.forEach {
            productInstanceRepository.create(it.productInstance, purchase.id)
            val item = Converters.purchaseItemToPurchaseItemDao(it)
            purchaseItemDao.insert(item)
        }
    }
}
