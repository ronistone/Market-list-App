package br.com.ronistone.marketlist.ui.purchase

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.databinding.PurchaseDetailsItemBinding
import br.com.ronistone.marketlist.model.PurchaseItem
import br.com.ronistone.marketlist.ui.home.AdapterItemsContract

class PurchaseItemAdapter(val purchaseViewModel: PurchaseViewModel, var items: List<PurchaseItem>): ListAdapter<PurchaseItem, PurchaseItemAdapter.ViewHolder>(ItemComparator()),
    AdapterItemsContract {

    var tracker: SelectionTracker<String>? = null

    class ViewHolder(val purchaseViewModel: PurchaseViewModel, val binding: PurchaseDetailsItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {

        var item: PurchaseItem? = null
        var itemAdapterPosition: Int? = null

        fun bind(item: PurchaseItem, position: Int) {
            binding.purchaseMarket.text = item.productInstance.product.name
            if(item.productInstance.price != null){
                binding.purchaseValue.text = "%d x R$ %.2f".format(item.quantity, item.productInstance.price / 100.0)
            } else {
                binding.purchaseValue.text = "%d x R$ ---".format(item.quantity)
            }

            binding.purchased.isChecked = item.purchased
            binding.purchased.setOnClickListener {
                Log.i("PURCHASE ITEM", item.toString())
                item.purchased = binding.purchased.isChecked
                purchaseViewModel.updateItem(it, item)
            }
            this.item = item
            this.itemAdapterPosition = position
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = itemAdapterPosition!!
                override fun getSelectionKey(): String = item?.id.toString()
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: PurchaseDetailsItemBinding = PurchaseDetailsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(purchaseViewModel, binding, parent.context)
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

    @SuppressLint("NotifyDataSetChanged")
    override fun replaceItems(items: List<*>) {
        this.items = items as List<PurchaseItem>
        this.submitList(items)
    }

    class ItemComparator : DiffUtil.ItemCallback<PurchaseItem>() {
        override fun areItemsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
