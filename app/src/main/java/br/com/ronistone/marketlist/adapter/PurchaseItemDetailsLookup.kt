package br.com.ronistone.marketlist.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.ui.purchase.PurchaseItemAdapter

class PurchaseItemDetailsLookup(
    private val recyclerView: RecyclerView
): ItemDetailsLookup<String>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return view?.let {
            (recyclerView.getChildViewHolder(it) as PurchaseItemAdapter.ViewHolder).getItemDetails()
        }
    }
}
