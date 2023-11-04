package br.com.ronistone.marketlist.dao.model

import androidx.room.Embedded
import androidx.room.Relation

class PurchaseWithDependencies(

    @Embedded
    var purchase: Purchase,

    @Relation( parentColumn = "marketId", entityColumn = "id" )
    val market: Market? = null,

    @Relation( parentColumn = "userId", entityColumn = "id" )
    val user: User? = null,

    @Relation(
        entity = PurchaseItem::class,
        parentColumn = "id",
        entityColumn = "purchaseId"
    )
    val items: List<PurchaseItemWithDependencies>? = null,

) {


    override fun toString(): String {
        return "PurchaseWithDependencies(purchase=$purchase, market=$market, user=$user, items=$items)"
    }
}
