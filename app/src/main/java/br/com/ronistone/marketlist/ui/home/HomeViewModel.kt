package br.com.ronistone.marketlist.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.repository.PurchaseRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val purchases = MutableLiveData<List<Purchase>>()

    val text: LiveData<String> = MutableLiveData<String>().apply {
        value = "Você ainda não tem uma lista feita!"
    }
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception handled: ${throwable.localizedMessage}")}

    fun fetch(view: View) {
        val repository = PurchaseRepository.getInstance(view.context)

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                repository.fetchPurchases(purchases)
            } catch (e: Exception) {
                Log.e("FETCH", "Fail to Fetch", e)
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
