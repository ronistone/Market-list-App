package br.com.ronistone.marketlist.dao.model

import androidx.room.Embedded
import androidx.room.Relation

class PurchaseItemWithDependencies (

    @Embedded
    var purchaseItem: PurchaseItem,

    @Relation( entity = ProductInstance::class, parentColumn = "productInstanceId", entityColumn = "id" )
    var productInstance: ProductInstanceWithDependencies

) {
}
