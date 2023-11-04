package br.com.ronistone.marketlist.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.ronistone.marketlist.ui.createPurchase.MarketHolder
import java.util.Date

@Entity
open class Market(
    @PrimaryKey val id: Int?,
    val name: String,
    val createdAt: Date?,
    val updatedAt: Date?,
) {
    constructor(name: String) : this(
        name = name,
        id = null,
        createdAt =  null,
        updatedAt = null
    )

    override fun toString(): String {
        return "Market(id=$id, name='$name', createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
