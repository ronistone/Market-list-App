package br.com.ronistone.marketlist.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class BaseViewModelOperations : ViewModel() {

    var jobs: List<Job> = mutableListOf()

    fun processRequest(view: View, errorMessage: String, request: suspend () -> Unit) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                request()
            } catch (e: Exception) {
                if(this.isActive) {
                    onError(view, errorMessage, e)
                }
            }
        }
        jobs += job

        CoroutineScope(Dispatchers.IO).launch {
            jobs = jobs.filter {
                !it.isCompleted
            }
        }
    }

    open fun onError(view: View, message: String, e: Throwable) {
        Log.e("VIEW MODEL ERROR", "$message -- Exception handled: ${e.localizedMessage}", e)
    }


    override fun onCleared() {
        super.onCleared()
        jobs.filter { !it.isCompleted }
            .forEach { it.cancel() }
    }
}
