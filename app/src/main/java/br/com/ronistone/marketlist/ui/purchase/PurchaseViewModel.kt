package br.com.ronistone.marketlist.ui.purchase

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import br.com.ronistone.marketlist.repository.PurchaseRepository
import br.com.ronistone.marketlist.ui.BaseViewModelOperations
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

class PurchaseViewModel : BaseViewModelOperations() {

    val purchase = MutableLiveData<Purchase?>()

    fun fetch(view: View, id: Int) {
        val purchaseRepository = PurchaseRepository.getInstance(view.context)
        val message = "Não foi possivel buscar essa compra"
        processRequest(view, message) {
            try {
                purchaseRepository.fetchPurchase(id, purchase)
            } catch (e: Exception) {
                Log.e("FETCH", "Fail to Fetch", e)
                Snackbar.make(view, "Não foi possível carregar seus itens", Snackbar.LENGTH_LONG).show()
                purchase.postValue(null)
            }
        }

    }

    fun updateItem(view: View, item: PurchaseItem) {
        val itemRepository = PurchaseRepository.getInstance(view.context)
        val message = "Não foi possível atualizar o item"
        processRequest(view, message) {
            val isSuccessful = itemRepository.updatePurchaseItem(item, purchase)
            if(!isSuccessful) {
                onError(view, message, RuntimeException("Fail to update item"))
            }
        }
    }

    fun removeItem(view: View, item: PurchaseItem) {
        val itemRepository = PurchaseRepository.getInstance(view.context)
        val message = "Não foi possível deletar o item"
        processRequest(view, message) {
            val isSuccessful = itemRepository.removePurchaseItem(item, purchase)
            if(!isSuccessful) {
                onError(view, message, RuntimeException("Fail to update item"))
            }
        }
    }

    override fun onError(view: View, message: String, e: Throwable) {
        super.onError(view, message, e)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        purchase.postValue(null)
    }
}
