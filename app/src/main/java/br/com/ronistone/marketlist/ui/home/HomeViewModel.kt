package br.com.ronistone.marketlist.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.data.PurchaseApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    val purchaseRepository: PurchaseApi = PurchaseApi.create()
    val purchases = MutableLiveData<List<Purchase>>()

    val text: LiveData<String> = MutableLiveData<String>().apply {
        value = "Você ainda não tem uma lista feita!"
    }
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception handled: ${throwable.localizedMessage}")}

    fun fetch(view: View) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = purchaseRepository.listPurchases()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        purchases.postValue(response.body())
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(view, "Não foi possível carregar suas compras", Snackbar.LENGTH_LONG).show()
                purchases.postValue(emptyList())
            }
        }
    }

    private fun onError(message: String) {
        Log.e("HOME VIEW MODEL ERROR", message)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
