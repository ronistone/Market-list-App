package br.com.ronistone.marketlist.model

import java.util.Date

data class Purchase(
    val user: User? = null,
    val id: Int? = null,
    val market: Market? = null,
    val createdAt: Date? = null,
    val items: List<PurchaseItem>? = null,
    val marketId: Int? = null,
    val userId: Int? = null,
    var totalSpent: Long = 0,
    var totalExpected: Long = 0,
): Comparable<Purchase> {
    override fun compareTo(other: Purchase): Int {
        if((this.createdAt?.compareTo(other.createdAt) ?: 0) == 0) {
            return other.market?.let { this.market?.name?.compareTo(it.name) } ?: 0
        }
        return this.createdAt?.compareTo(other.createdAt) ?: 0
    }

    override fun toString(): String {
        return "Purchase(user=$user, id=$id, market=$market, createdAt=$createdAt, items=$items, marketId=$marketId, userId=$userId, totalSpent=$totalSpent, totalExpected=$totalExpected)"
    }
}
