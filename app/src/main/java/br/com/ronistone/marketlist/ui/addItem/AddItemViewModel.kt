package br.com.ronistone.marketlist.ui.addItem

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.model.Product
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import br.com.ronistone.marketlist.repository.ProductRepository
import br.com.ronistone.marketlist.repository.PurchaseItemRepository
import br.com.ronistone.marketlist.repository.PurchaseRepository
import br.com.ronistone.marketlist.ui.BaseViewModelOperations
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddItemViewModel : BaseViewModelOperations() {

    val productsSearch = MutableLiveData<List<Product>>()
    val selectedProduct = MutableLiveData<PurchaseItem?>()
    val purchase = MutableLiveData<Purchase>()

    var purchaseId = MutableLiveData<Int?>()

    var purchaseItemId = MutableLiveData<Int?>()

    val isEdition get() = (purchaseItemId.value != null)

    fun selectProduct(product: Product) {
        val value = selectedProduct.value?.copyChangingProduct(product) ?: PurchaseItem(
            product = product,
        )

        selectedProduct.postValue(value)
    }


    fun queryName(view: View, name: String) {
        val productRepository = ProductRepository.getInstance(view.context)
        val errorMessage = "Failed to Query Products with this name $name"
        processRequest(view, errorMessage) {
            productRepository.getByName(name, productsSearch)
        }
    }


    fun queryEan(view: View, ean: String, onFinish: () -> Unit) {
        val productRepository = ProductRepository.getInstance(view.context)
        val errorMessage = "Failed to Query Product with this Ean $ean"
        processRequest(view, errorMessage) {
            val isSuccessful = productRepository.getByEan(ean, selectedProduct)
            withContext(Dispatchers.Main) {
                if (!isSuccessful) {
                    val selected = selectedProduct.value ?: PurchaseItem(product = Product())
                    selectedProduct.postValue(
                        selected.copyChangingProduct(product = selected.product
                            .copy(ean = ean, id = null)
                        )
                    )
                    onFinish()
                }
            }
        }
    }

    fun addItem(view: View, purchaseItem: PurchaseItem, purchaseId: Int, onsuccess: (() -> Unit)? = null) {
        val purchaseRepository = PurchaseRepository.getInstance(view.context)
        val errorMessage = "Failed to Add item"
        processRequest(view, errorMessage) {
            val isSuccessful = purchaseRepository.addPurchaseItem(purchaseItem, purchaseId)
            withContext(Dispatchers.Main) {
                if (isSuccessful) {
                    onsuccess?.let { it() }
                }
            }
        }
    }

    fun updateItem(view: View, purchaseItem: PurchaseItem, purchaseId: Int, onsuccess: (() -> Unit)? = null) {
        val purchaseRepository = PurchaseRepository.getInstance(view.context)
        val errorMessage = "Failed to Update item"
        processRequest(view, errorMessage) {
            val response = purchaseRepository.updatePurchaseItem(purchaseItem, purchaseId)
            withContext(Dispatchers.Main) {
                if (response != null) {
                    onsuccess?.let { it() }
                }
            }
        }
    }

    fun fetchItem(view: View, purchaseId: Int, purchaseItemId: Int) {
        val purchaseItemRepository = PurchaseItemRepository.getInstance(view.context)
        val errorMessage = "Failed to Get item"
        processRequest(view, errorMessage) {
            purchaseItemRepository.fetchItem(purchaseId, purchaseItemId, selectedProduct)
        }
    }

    override fun onError(view: View, message: String, e: Throwable) {
        super.onError(view, message, e)
        Log.e("ADD ITEM VIEW MODEL ERROR", message)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

}
