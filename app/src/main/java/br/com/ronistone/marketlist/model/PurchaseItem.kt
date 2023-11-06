package br.com.ronistone.marketlist.model

import br.com.ronistone.marketlist.adapter.ItemHolder
import java.util.Date

data class PurchaseItem @JvmOverloads constructor(
    val id: Int? = null,
    val product: Product,
    val quantity: Int = 1,
    var purchased: Boolean = false,
    val price: Int? = null,
    val createdAt: Date? = null
) : ItemHolder, Comparable<PurchaseItem> {

    fun copyChangingProduct(product: Product): PurchaseItem {
        return this.copy(product = product)
    }

    fun name(): String? {
        return this.product.name
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
        return "PurchaseItem(id=$id, product=$product, quantity=$quantity, purchased=$purchased, price=$price, createdAt=$createdAt)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PurchaseItem

        if (id != other.id) return false
        if (product != other.product) return false
        if (quantity != other.quantity) return false
        if (purchased != other.purchased) return false
        if (price != other.price) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + product.hashCode()
        result = 31 * result + quantity
        result = 31 * result + purchased.hashCode()
        result = 31 * result + (price ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        return result
    }
}
