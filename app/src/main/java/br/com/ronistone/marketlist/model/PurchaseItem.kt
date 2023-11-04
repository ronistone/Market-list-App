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

    constructor(product: Product) : this(productInstance = ProductInstance(product = product))

    fun copyChangingProduct(product: Product): PurchaseItem {
        return this.copy(productInstance = productInstance.copy(product = product))
    }

    override fun toString(): String {
        return "PurchaseItem(id=$id, purchase=$purchase, productInstance=$productInstance, createdAt=$createdAt, quantity=$quantity, purchased=$purchased)"
    }
}
