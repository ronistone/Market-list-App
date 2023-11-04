package br.com.ronistone.marketlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ronistone.marketlist.dao.model.ProductInstance

@Dao
interface ProductInstanceDao {
    @Query("SELECT * FROM productinstance")
    fun getAll(): List<ProductInstance>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insert(productInstance: ProductInstance)

    @Query("DELETE FROM productinstance WHERE id in (SELECT p.productInstanceId FROM purchaseitem p WHERE purchaseId = :purchaseId)")
    fun cleanTable(purchaseId: Int)
}
