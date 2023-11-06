package br.com.ronistone.marketlist.dao.model

import androidx.room.Embedded
import androidx.room.Relation

class PurchaseItemWithDependencies (

    @Embedded
    var purchaseItem: PurchaseItem,

    @Relation( parentColumn = "productId", entityColumn = "id" )
    val product: Product? = null

) {
}
