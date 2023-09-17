package br.com.ronistone.marketlist.model

import java.util.Date

data class PurchaseItem @JvmOverloads constructor(
    val id: Int? = null,
    val purchase: Purchase? = null,
    val productInstance: ProductInstance,
    val createdAt: Date? = null,
    val quantity: Int = 1,
    var purchased: Boolean = false
) {

    override fun toString(): String {
        return "PurchaseItem(id=$id, purchase=$purchase, productInstance=$productInstance, createdAt=$createdAt, quantity=$quantity, purchased=$purchased)"
    }
}
