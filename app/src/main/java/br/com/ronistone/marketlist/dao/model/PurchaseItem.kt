package br.com.ronistone.marketlist.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class PurchaseItem @JvmOverloads constructor(
    @PrimaryKey
    var id: Int? = null,

    var createdAt: Date? = null,
    var quantity: Int = 1,
    var purchased: Boolean = false,
    var purchaseId: Int? = null,
    val productId: Int,
    val price: Int? = null,

) {

    override fun toString(): String {
        return "PurchaseItem(id=$id, createdAt=$createdAt, quantity=$quantity, purchased=$purchased, purchaseId=$purchaseId, productId=$productId, price=$price)"
    }
}
