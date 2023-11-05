package br.com.ronistone.marketlist.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ListItemDetailsLookup(
    private val recyclerView: RecyclerView
): ItemDetailsLookup<String>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return view?.let {
            (recyclerView.getChildViewHolder(it) as ItemHolderDetails).getItemDetails()
        }
    }
}
