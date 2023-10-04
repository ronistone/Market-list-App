package br.com.ronistone.marketlist.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import br.com.ronistone.marketlist.model.Product

class ProductNameArrayAdapter(
    private val context: Context,
    layoutId: Int,
    private var elements: MutableList<Product>): ArrayAdapter<Product>(context, layoutId, elements), Filterable {

    override fun getFilter(): Filter {
        return filter
    }

    fun update(newElements: MutableList<Product>) {
        this.clear()
        this.addAll(newElements)
        this.notifyDataSetChanged()
    }


    private var filter: Filter = object:Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filter = FilterResults()

            filter.count = elements.size
            filter.values = elements

            return filter
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            elements = results?.values as MutableList<Product>
            notifyDataSetInvalidated()
        }

    }
}
