package br.com.ronistone.marketlist.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.ronistone.marketlist.Database
import br.com.ronistone.marketlist.dao.MarketDao
import br.com.ronistone.marketlist.dao.model.Converters
import br.com.ronistone.marketlist.data.MarketApi
import br.com.ronistone.marketlist.model.Market
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class MarketRepository(
    private val marketDao: MarketDao,
    private val marketApi: MarketApi
) {

    companion object {
        private var instance: MarketRepository? = null

        fun getInstance(context: Context): MarketRepository {
            if (instance == null) {
                synchronized(this){
                    if(instance == null) {
                        instance = MarketRepository(
                            Database.getDatabase(context)!!.marketDao(),
                            MarketApi.create()
                        )
                    }
                }
            }
            return instance!!
        }
    }

    suspend fun fetch(response: MutableLiveData<List<Market>>) {

        val markets = marketDao.getAll()
        Log.i("DATABASE", markets.toString())
        val marketsConverted: List<Market> = markets.map { Converters.marketDaoToMarket(it)!! }
        response.postValue(marketsConverted.sorted())

        val apiResponse = marketApi.listMarkets()
        withContext(Dispatchers.Main) {
            if (apiResponse.isSuccessful) {
                response.postValue(apiResponse.body()?.sorted())

            } else {
                throw RuntimeException("Fail to fetch")
            }
        }
        update(apiResponse.body())
    }

    suspend fun create(market: Market): Boolean {
        val response = marketApi.createMarket(market)
        response.body()?.let { marketDao.insert(Converters.marketToMarketDao(it)!!) }
        return response.isSuccessful
    }
    fun update(markets: List<Market>?) {
        marketDao.cleanTable()
        markets?.forEach {
            val market = Converters.marketToMarketDao(it)
            marketDao.insert(market!!)
        }
    }

}
