package br.com.ronistone.marketlist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ronistone.marketlist.dao.model.PurchaseItem
import br.com.ronistone.marketlist.dao.model.PurchaseItemWithDependencies

@Dao
interface PurchaseItemDao {

    @Query("SELECT * FROM purchaseitem WHERE purchaseId = :purchaseId")
    fun getAllByPurchase(purchaseId: Int): List<PurchaseItemWithDependencies>

    @Query("SELECT * FROM purchaseitem WHERE purchaseId = :purchaseId AND id = :purchaseItemId")
    fun getItem(purchaseId: Int, purchaseItemId: Int): PurchaseItemWithDependencies?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(purchaseItems: PurchaseItem)

    @Query("DELETE FROM purchaseitem WHERE purchaseId = :purchaseId")
    fun cleanPurchaseItems(purchaseId: Int)

}
