package br.com.ronistone.marketlist.ui.purchase

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ronistone.marketlist.data.PurchaseApi
import br.com.ronistone.marketlist.model.Purchase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PuchaseViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val purchaseRepository: PurchaseApi = PurchaseApi.create()

    val purchase = MutableLiveData<Purchase?>()

    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception handled: ${throwable.localizedMessage}")}

    fun fetch(view: View, id: Int) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = purchaseRepository.getPurchase(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        purchase.postValue(response.body())
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(view, "Não foi possível carregar sua compra", Snackbar.LENGTH_LONG).show()
                purchase.postValue(null)
            }
        }
    }

    private fun onError(message: String) {
        Log.e("PURCHASE VIEW MODEL ERROR", message)
    }
}
