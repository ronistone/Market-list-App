package br.com.ronistone.marketlist.model

import java.util.Date

data class ProductInstance @JvmOverloads constructor(
    val id: Int? = null,
    val product: Product,
    val market: Market? = null,
    val price: Int? = null,
    val createdAt: Date? = null
) {
    override fun toString(): String {
        return "ProductInstance(id=$id, product=$product, market=$market, price=$price, createdAt=$createdAt)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductInstance

        if (id != other.id) return false
        if (product != other.product) return false
        if (market != other.market) return false
        if (price != other.price) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + product.hashCode()
        result = 31 * result + (price ?: 0)
        return result
    }


}
