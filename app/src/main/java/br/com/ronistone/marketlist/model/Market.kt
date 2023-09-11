package br.com.ronistone.marketlist.model

import br.com.ronistone.marketlist.ui.createPurchase.MarketHolder
import java.util.Date

open class Market(
    val id: Long?,
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

    fun toHolder(): MarketHolder {
        return MarketHolder(
            id=this.id,
            name=this.name,
            createdAt=this.createdAt,
            updatedAt=this.updatedAt
        )
    }
}
