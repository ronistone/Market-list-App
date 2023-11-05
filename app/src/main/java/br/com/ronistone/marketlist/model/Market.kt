package br.com.ronistone.marketlist.model

import br.com.ronistone.marketlist.adapter.ItemHolder
import br.com.ronistone.marketlist.ui.createPurchase.MarketHolder
import java.util.Date

open class Market(
    val id: Int?,
    val name: String,
    val createdAt: Date?,
    val updatedAt: Date?,
): Comparable<Market>, ItemHolder {
    constructor(name: String) : this(
        name = name,
        id = null,
        createdAt =  null,
        updatedAt = null
    )

    override fun id(): Int {
        return id ?: 0
    }

    override fun compareTo(other: Market): Int {
        if(this.name == other.name) {
            return other.id?.let { this.id?.compareTo(it) } ?: 0
        }

        return this.name.compareTo(other.name)
    }

    override fun toString(): String {
        return "Market(id=$id, name='$name', createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    fun toHolder(): MarketHolder {
        return MarketHolder(
            id=this.id,
            name=this.name,
            createdAt=this.createdAt,
            updatedAt=this.updatedAt
        )
    }
}
