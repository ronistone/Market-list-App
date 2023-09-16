package br.com.ronistone.marketlist.adapter

import androidx.recyclerview.selection.ItemKeyProvider
import br.com.ronistone.marketlist.ui.purchase.PurchaseItemAdapter

class PurchaseItemKeyProvider(
    private val adapter: PurchaseItemAdapter
): ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String =
        adapter.currentList[position].id.toString()

    override fun getPosition(key: String): Int =
        adapter.currentList.indexOfFirst { it.id.toString() == key }
}
