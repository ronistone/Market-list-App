package br.com.ronistone.marketlist.model

import java.util.Date

data class Purchase(
    val user: User? = null,
    val id: Int? = null,
    val market: Market? = null,
    val createdAt: Date? = null,
    val items: List<PurchaseItem>? = null,
    val marketId: Long? = null,
    val userId: Long? = null,
    var totalSpent: Long = 0,
    var totalExpected: Long = 0,
) {
    override fun toString(): String {
        return "Purchase(user=$user, id=$id, market=$market, createdAt=$createdAt, items=$items, marketId=$marketId, userId=$userId, totalSpent=$totalSpent, totalExpected=$totalExpected)"
    }
}
