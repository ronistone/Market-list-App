package br.com.ronistone.marketlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.ronistone.marketlist.dao.model.Purchase
import br.com.ronistone.marketlist.dao.model.PurchaseWithDependencies

@Dao
interface PurchaseDao {

    @Query("SELECT * FROM purchase")
    @Transaction
    fun getAll(): List<PurchaseWithDependencies>

    @Query("SELECT * FROM purchase WHERE purchase.id = :id")
    fun getById(id: Int): PurchaseWithDependencies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(purchases : Purchase)

    @Query("DELETE FROM purchase")
    fun cleanTable()

}
