package br.com.ronistone.marketlist.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity
data class Purchase(

    @PrimaryKey val id: Int? = null,
    val marketId: Int? = null,
    val userId: Int? = null,

    val createdAt: Date? = null,

    var totalSpent: Long = 0,
    var totalExpected: Long = 0,

) {

    override fun toString(): String {
        return "Purchase(id=$id, marketId=$marketId, userId=$userId, createdAt=$createdAt, totalSpent=$totalSpent, totalExpected=$totalExpected)"
    }
}
