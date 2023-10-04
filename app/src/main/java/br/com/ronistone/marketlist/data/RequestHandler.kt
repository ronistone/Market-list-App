package br.com.ronistone.marketlist.data

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class RequestHandler: ViewModel() {

    var job: Job? = null

    fun processRequest(view: View, errorMessage: String, request: suspend () -> Unit) {
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                request()
            } catch (e: Exception) {
                Log.e("Process Request", "$e ${e.message} ${e.stackTrace}")
                onError(view, errorMessage)
            }
        }
    }


    fun onError(view: View, message: String) {
        Log.e("PURCHASE VIEW MODEL ERROR", message)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }


}
