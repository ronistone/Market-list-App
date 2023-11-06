package br.com.ronistone.marketlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ronistone.marketlist.dao.model.Product


@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insert(product: Product)

    @Query("""
       DELETE FROM product WHERE id in (
            SELECT p.id FROM product p 
                        INNER JOIN purchaseitem pu ON pu.productId = p.id
                WHERE pu.purchaseId = :purchaseId
            ) 
    """)
    fun cleanTable(purchaseId: Int)
}
