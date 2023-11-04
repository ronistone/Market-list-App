package br.com.ronistone.marketlist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ronistone.marketlist.dao.model.Market

@Dao
interface MarketDao {

    @Query("SELECT * FROM market")
    fun getAll(): List<Market>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insert(market: Market)

    @Query("DELETE FROM market")
    fun cleanTable()
}
