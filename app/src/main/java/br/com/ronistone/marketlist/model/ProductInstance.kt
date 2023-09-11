package br.com.ronistone.marketlist.model

import java.util.Date

data class ProductInstance(
    val id: Int,
    val product: Product,
    val market: Market,
    val price: Int,
    val precision: Int,
    val createdAt: Date
) {
    override fun toString(): String {
        return "ProductInstance(id=$id, product=$product, market=$market, price=$price, precision=$precision, createdAt=$createdAt)"
    }
}
