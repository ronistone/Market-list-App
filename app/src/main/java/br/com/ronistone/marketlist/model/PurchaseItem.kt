package br.com.ronistone.marketlist.model

import br.com.ronistone.marketlist.adapter.ItemHolder
import java.util.Date

data class PurchaseItem @JvmOverloads constructor(
    val id: Int? = null,
    val purchase: Purchase? = null,
    val productInstance: ProductInstance,
    val createdAt: Date? = null,
    val quantity: Int = 1,
    var purchased: Boolean = false
) : ItemHolder, Comparable<PurchaseItem> {

    constructor(product: Product) : this(productInstance = ProductInstance(product = product))

    fun copyChangingProduct(product: Product): PurchaseItem {
        return this.copy(productInstance = productInstance.copy(product = product))
    }

    fun name(): String? {
        return this.productInstance.product.name
    }

    override fun compareTo(other: PurchaseItem): Int {
        if(other.purchased == this.purchased && this.name() == other.name()) {
            return other.id?.let { this.id?.compareTo(it) } ?: 0
        }

        if (other.purchased == this.purchased) {
            return other.name()?.let { this.name()?.compareTo(it) } ?: 0
        }

        return this.purchased.compareTo(other.purchased)
    }

    override fun id(): Int {
        return id ?: 0
    }

    override fun toString(): String {
        return "PurchaseItem(id=$id, purchase=$purchase, productInstance=$productInstance, createdAt=$createdAt, quantity=$quantity, purchased=$purchased)"
    }
}
