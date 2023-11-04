package br.com.ronistone.marketlist.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
class ProductInstance(
    @PrimaryKey
    val id: Int? = null,
    val productId: Int,
    val marketId: Int? = null,
    val price: Int? = null,
    val createdAt: Date? = null
) {
}
