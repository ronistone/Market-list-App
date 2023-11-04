package br.com.ronistone.marketlist.ui.createMarket

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.ronistone.marketlist.data.MarketApi
import br.com.ronistone.marketlist.model.Market
import br.com.ronistone.marketlist.repository.MarketRepository
import br.com.ronistone.marketlist.ui.home.AdapterItemsContract
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CreateMarketViewModel : ViewModel() {

    val marketName: MutableLiveData<String> = MutableLiveData<String>()

    var marketCreateJob: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError("Exception handled: ${throwable.localizedMessage}")}

    fun updateName(name: String) {
        marketName.postValue(name)
    }

    fun create(context: Context, view: View, callbackFunction: () -> Unit ): Job? {
        val marketRepository = MarketRepository.getInstance(context)
        marketCreateJob = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            if(marketName.value == null || marketName.value!!.isEmpty()) {
                Snackbar.make(context, view, "Nome está inválido", Snackbar.LENGTH_LONG).show()
            } else {
                try {
                    val isSuccessful = marketRepository.create(Market(name = marketName.value!!))
                    if(isSuccessful) {
                        withContext(Dispatchers.Main) {
                            callbackFunction()
                        }
                    } else {
                        Snackbar.make(context, view, "Falha ao criar o novo Mercado, tente novamente", Snackbar.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e("CREATE MARKET", "Fail to create market", e)
                    Snackbar.make(context, view, "Falha ao criar o novo Mercado, tente novamente", Snackbar.LENGTH_LONG).show()
                }

            }

        }
        return marketCreateJob

    }

    private fun onError(message: String) {
        Log.e("CREATE MARKET ERROR", message)
    }
}
