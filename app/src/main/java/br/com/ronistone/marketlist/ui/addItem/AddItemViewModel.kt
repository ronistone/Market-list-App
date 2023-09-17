package br.com.ronistone.marketlist.ui.addItem

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.data.ProductApi
import br.com.ronistone.marketlist.data.PurchaseItemApi
import br.com.ronistone.marketlist.data.RequestHandler
import br.com.ronistone.marketlist.model.Product
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddItemViewModel : RequestHandler() {
    val productApi: ProductApi = ProductApi.create()
    val purchaseItemApi: PurchaseItemApi = PurchaseItemApi.create()

    val productsSearch = MutableLiveData<List<Product>>()
    val selectedProduct = MutableLiveData<Product>()
    val purchase = MutableLiveData<Purchase>()


    fun queryName(view: View, name: String) {
        val errorMessage = "Failed to Query Products with this name $name"
        this.processRequest(view, errorMessage) {
            val response = productApi.getProductsByName(name)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productsSearch.postValue(response.body())
                } else {
                    Log.e("AddItemViewModel", "Error: ${response.message()}")
                    onError(view, errorMessage)
                }
            }
        }
    }


    fun queryEan(view: View, ean: String) {
        val errorMessage = "Failed to Query Product with this Ean $ean"
        this.processRequest(view, errorMessage) {
            val response = productApi.getProductByEan(ean)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    selectedProduct.postValue(response.body())
                } else {
                    Log.e("AddItemViewModel", "Error: ${response.message()}")
                    onError(view, errorMessage)
                }
            }
        }
    }

    fun addItem(view: View, purchaseItem: PurchaseItem, onsuccess: (() -> Unit)? = null) {
        val errorMessage = "Failed to Add item"
        this.processRequest(view, errorMessage) {
            val response = purchaseItemApi.addItem(purchaseItem.purchase?.id!!, purchaseItem)
            withContext(Dispatchers.Main) {
                if (!response.isSuccessful) {
                    Log.e("AddItemViewModel", "Error: ${response.message()}")
                    onError(view, errorMessage)
                } else {
                    onsuccess?.let { it() }
                }
            }
        }
    }

}
