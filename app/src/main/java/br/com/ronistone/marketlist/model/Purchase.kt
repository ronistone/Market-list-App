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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Purchase

        if (user != other.user) return false
        if (id != other.id) return false
        if (market != other.market) return false
        if (createdAt != other.createdAt) return false
        if (items != other.items) return false
        if (marketId != other.marketId) return false
        if (userId != other.userId) return false
        if (totalSpent != other.totalSpent) return false
        if (totalExpected != other.totalExpected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user?.hashCode() ?: 0
        result = 31 * result + (id ?: 0)
        result = 31 * result + (market?.hashCode() ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (items?.hashCode() ?: 0)
        result = 31 * result + (marketId ?: 0)
        result = 31 * result + (userId ?: 0)
        result = 31 * result + totalSpent.hashCode()
        result = 31 * result + totalExpected.hashCode()
        return result
    }


}
