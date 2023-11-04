package br.com.ronistone.marketlist.ui.createPurchase

import br.com.ronistone.marketlist.model.Market
import java.util.Date

class MarketHolder(
    id: Int?,
    name: String,
    createdAt: Date?,
    updatedAt: Date?,
): Market(id, name, createdAt, updatedAt) {

    constructor(name: String) : this(
        name = name,
        id = null,
        createdAt =  Date(),
        updatedAt = Date()
    )

    override fun toString(): String {
        return super.name
    }
}
