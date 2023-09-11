package br.com.ronistone.marketlist.ui.createPurchase

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ronistone.marketlist.data.MarketApi
import br.com.ronistone.marketlist.data.PurchaseApi
import br.com.ronistone.marketlist.model.Market
import br.com.ronistone.marketlist.model.Purchase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePurchaseViewModel : ViewModel() {
    val marketApi: MarketApi = MarketApi.create()
    val purchaseApi: PurchaseApi = PurchaseApi.create()

    val markets = MutableLiveData<List<Market>>()
    val selectedMarket = MutableLiveData<Market>()


    var marketJob: Job? = null
    var purchaseJob: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception handled: ${throwable.localizedMessage}")}


    fun loadMarkets(view: View) {
        marketJob = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = marketApi.listMarkets()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        markets.postValue(response.body())
                    } else {
                        onError("Error: ${response.message()}")
                        markets.postValue(emptyList())
                    }
                }
            } catch (e: Exception) {
                markets.postValue(emptyList())
                Snackbar.make(view, "Não foi possível carregar os mercados disponíveis", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun createPurchase(context: Context, view: View, callback: () -> Unit) {
        purchaseJob = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if(selectedMarket.value?.id == null) {
                Snackbar.make(context, view, "Você precisa selecionar um mercado antes!", Snackbar.LENGTH_LONG).show()
                return@launch
            }
            val response = purchaseApi.createPurchase(Purchase(marketId = selectedMarket.value?.id, userId = 1))
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    callback()

                } else {
                    Snackbar.make(context, view, "Falha ao iniciar Compra, tente novamente", Snackbar.LENGTH_LONG).show()
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        Log.e("CREATE PURCHASE ERROR", message)
    }

    override fun onCleared() {
        super.onCleared()
        marketJob?.cancel()
    }
}
