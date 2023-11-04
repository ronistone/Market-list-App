package br.com.ronistone.marketlist.repository

import android.content.Context
import br.com.ronistone.marketlist.Database
import br.com.ronistone.marketlist.dao.ProductInstanceDao
import br.com.ronistone.marketlist.dao.model.Converters
import br.com.ronistone.marketlist.model.ProductInstance

class ProductInstanceRepository private constructor(
    private val productInstanceDao: ProductInstanceDao,
    private val productRepository: ProductRepository
) {

    companion object {
        private var instance: ProductInstanceRepository? = null

        fun getInstance(context: Context): ProductInstanceRepository {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ProductInstanceRepository(
                            Database.getDatabase(context)!!.productInstanceDao(),
                            ProductRepository.getInstance(context)
                        )
                    }
                }
            }
            return instance!!
        }
    }

    suspend fun create(productInstance: ProductInstance, purchaseId: Int): Boolean {
        productRepository.create(productInstance.product, purchaseId)
        productInstanceDao.insert(Converters.productInstanceModelToProductInstanceDao(productInstance))
        return true
    }

    suspend fun cleanByPurchase(purchaseId: Int) {
        productRepository.cleanByPurchase(purchaseId)
        productInstanceDao.cleanTable(purchaseId)
    }
}
