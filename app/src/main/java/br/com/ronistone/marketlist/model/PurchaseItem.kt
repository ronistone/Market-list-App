package br.com.ronistone.marketlist.model

import java.util.Date

data class PurchaseItem(
    val id: Int,
    val purchase: Purchase,
    val productInstance: ProductInstance,
    val createdAt: Date,
    val quantity: Int,
    val purchased: Boolean
) {
    override fun toString(): String {
        return "PurchaseItem(id=$id, purchase=$purchase, productInstance=$productInstance, createdAt=$createdAt)"
    }
}
