package br.com.ronistone.marketlist.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.Database
import br.com.ronistone.marketlist.dao.MarketDao
import br.com.ronistone.marketlist.dao.PurchaseDao
import br.com.ronistone.marketlist.dao.PurchaseItemDao
import br.com.ronistone.marketlist.dao.model.Converters
import br.com.ronistone.marketlist.data.PurchaseApi
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class PurchaseRepository private constructor(
    private val purchaseApi: PurchaseApi,
    private val purchaseDao: PurchaseDao,
    private val marketDao: MarketDao,
    private val purchaseItemRepository: PurchaseItemRepository
) {

    companion object {
        private var instance: PurchaseRepository? = null

        fun getInstance(context: Context): PurchaseRepository {
            if (instance == null) {
                synchronized(this){
                    if(instance == null) {
                        instance = PurchaseRepository(
                            PurchaseApi.create(),
                            Database.getDatabase(context)!!.purchaseDao(),
                            Database.getDatabase(context)!!.marketDao(),
                            PurchaseItemRepository.getInstance(context)
                        )
                    }
                }
            }
            return instance!!
        }
    }

    suspend fun createPurchase(purchase: Purchase): Boolean {
        val response = purchaseApi.createPurchase(purchase)
        response.body()?.let { purchaseDao.insert(Converters.purchaseModelToPurchaseDao(it)) }
        return response.isSuccessful
    }

    suspend fun updatePurchaseItem(item: PurchaseItem, result: MutableLiveData<Purchase?>? = null): Boolean {
        val purchase = purchaseItemRepository.updateItem(item) ?: return false
        purchaseDao.insert(Converters.purchaseModelToPurchaseDao(purchase))
        result?.let {
            withContext(Dispatchers.Main) {
                result.postValue(purchase)
            }
        }

        return true
    }

    suspend fun removePurchaseItem(item: PurchaseItem, result: MutableLiveData<Purchase?>): Boolean {
        val purchase = purchaseItemRepository.removeItem(item) ?: return false
        purchaseDao.insert(Converters.purchaseModelToPurchaseDao(purchase))
        withContext(Dispatchers.Main) {
            result.postValue(purchase)
        }
        return true
    }

    suspend fun addPurchaseItem(item: PurchaseItem): Boolean {
        val purchase = purchaseItemRepository.addItem(item) ?: return false
        purchaseDao.insert(Converters.purchaseModelToPurchaseDao(purchase))
        return true
    }

    suspend fun fetchPurchase(id: Int, resul: MutableLiveData<Purchase?>): Boolean {
        val purchase = purchaseDao.getById(id)

        val purchaseResult = Converters.purchaseWithDependenciesToPurchase(purchase)

        resul.postValue(purchaseResult)

        val apiResponse = purchaseApi.getPurchase(id)
        withContext(Dispatchers.Main) {
            if(apiResponse.isSuccessful) {
                resul.postValue(apiResponse.body())
            } else {
                throw RuntimeException("Fail to fetch")
            }
        }

        purchaseItemRepository.updatePurchase(apiResponse.body()!!)
        purchaseDao.insert(Converters.purchaseModelToPurchaseDao(apiResponse.body()!!))

        return true
    }

    suspend fun fetchPurchases(response: MutableLiveData<List<Purchase>>) {

        val purchases = purchaseDao.getAll()
        Log.i("DATABASE", purchases.toString())
        response.postValue(purchases.map { Converters.purchaseWithDependenciesToPurchase(it) })

        val apiResponse = purchaseApi.listPurchases()
        withContext(Dispatchers.Main) {
            if (apiResponse.isSuccessful) {
                response.postValue(apiResponse.body())

            } else {
                throw RuntimeException("Fail to fetch")
            }
        }
        updatePurchases(apiResponse.body())
    }
    fun updatePurchases(purchases: List<Purchase>?) {
        purchaseDao.cleanTable()
        purchases?.forEach {
            val purchase = Converters.purchaseToPurchaseWithDependencies(it)
            purchase.market.let { it1 -> marketDao.insert(it1!!) }
            purchaseDao.insert(purchase.purchase)
        }
    }



}
