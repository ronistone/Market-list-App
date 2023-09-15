package br.com.ronistone.marketlist.ui.purchase

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.databinding.PurchaseDetailsItemBinding
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.databinding.PurchaseItemBinding
import br.com.ronistone.marketlist.model.PurchaseItem
import br.com.ronistone.marketlist.ui.home.AdapterItemsContract

class PurchaseItemAdapter(var items: List<PurchaseItem>): RecyclerView.Adapter<PurchaseItemAdapter.ViewHolder>(),
    AdapterItemsContract {


    class ViewHolder(val binding: PurchaseDetailsItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PurchaseItem) {
            binding.purchaseMarket.text = item.productInstance.product.name
            val price = item.productInstance.price / 100.0
            binding.purchaseValue.text = "%d x R$ %.2f".format(item.quantity, price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: PurchaseDetailsItemBinding = PurchaseDetailsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun replaceItems(items: List<*>) {
        this.items = items as List<PurchaseItem>
        notifyDataSetChanged()
    }

}
