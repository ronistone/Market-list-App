package br.com.ronistone.marketlist.ui.createPurchase

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.adapter.ItemComparator
import br.com.ronistone.marketlist.adapter.ItemHolderDetails
import br.com.ronistone.marketlist.databinding.MarketItemBinding
import br.com.ronistone.marketlist.model.Market
import br.com.ronistone.marketlist.ui.home.AdapterItemsContract

class CreatePurchaseMarketAdapter(
    var items: List<Market>,
    var viewModel: CreatePurchaseViewModel
) : ListAdapter<Market, CreatePurchaseMarketAdapter.ViewHolder>(ItemComparator<Market>()), AdapterItemsContract {

    var tracker: SelectionTracker<String>? = null
    class ViewHolder(val binding: MarketItemBinding, viewModel: CreatePurchaseViewModel): ItemHolderDetails, RecyclerView.ViewHolder(binding.root) {
        private var item: Market? = null
        private var itemAdapterPosition: Int? = null

        private var marketName: TextView? = null
        private var marketSelect: RadioButton? = null

        init {
            viewModel.selectedMarket.observeForever {
                if(it.id == item?.id) {
                    marketSelect?.isChecked = true
                } else if (marketSelect?.isChecked == true) {
                    marketSelect?.isChecked = false
                }
            }
        }

        fun bind(market: Market, position: Int) {
            marketName = binding.marketName
            marketName!!.text = market.name
            marketSelect = binding.marketSelect
            marketSelect!!.isChecked = false

            this.itemAdapterPosition = position
            this.item = market
        }

        override fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = itemAdapterPosition!!
                override fun getSelectionKey(): String = item?.id.toString()
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MarketItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int {
        return this.currentList.size
    }

    fun onSelect(position: Int) {
        viewModel.selectedMarket.postValue(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
        tracker?.let {
            if(it.isSelected(getItem(position).id.toString())) {
                holder.itemView.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_item_selected)
            } else {
                holder.itemView.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_item)
            }
        }
    }

    override fun replaceItems(list: List<*>) {
        this.items = list as List<Market>
        this.submitList(this.items)
        this.notifyDataSetChanged()
    }

}
