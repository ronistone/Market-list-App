package br.com.ronistone.marketlist.data

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

class JsonConverterFactory {

    companion object {
        fun create(): GsonConverterFactory {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").create()
            return GsonConverterFactory
                .create(gson)

        }
    }

}
