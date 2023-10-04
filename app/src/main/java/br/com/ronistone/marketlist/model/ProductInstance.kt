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
}
