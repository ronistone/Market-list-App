package br.com.ronistone.marketlist.ui.createPurchase

import android.content.Context
import android.widget.ArrayAdapter
import br.com.ronistone.marketlist.ui.home.AdapterItemsContract

class CreatePurchaseMarketSpinnerAdapter(
    val parentContext: Context,
    val resource: Int,
    var items: MutableList<MarketHolder>
) : ArrayAdapter<MarketHolder>(parentContext, resource, items), AdapterItemsContract {

    var firstSelect = true
    var defaultValue = MarketHolder(
        name = "Selecione um Mercado"
    )

    var emptyValue = MarketHolder(
        name = "Você Ainda não tem mercado cadastrado!"
    )



    override fun replaceItems(list: List<*>) {
        super.clear()
        super.add(defaultValue)
        super.addAll(list as MutableList<MarketHolder>)
        notifyDataSetChanged()
    }

    fun replaceEmpty() {
        super.clear()
        super.add(emptyValue)
        notifyDataSetChanged()
    }

}
