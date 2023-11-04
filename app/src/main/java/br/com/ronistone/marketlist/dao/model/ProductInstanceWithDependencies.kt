package br.com.ronistone.marketlist.dao.model

import androidx.room.Embedded
import androidx.room.Relation

class ProductInstanceWithDependencies(

    @Embedded
    var productInstance: ProductInstance,

    @Relation( parentColumn = "marketId", entityColumn = "id" )
    val market: Market? = null,

    @Relation( parentColumn = "productId", entityColumn = "id" )
    val product: Product? = null,

) {
}
