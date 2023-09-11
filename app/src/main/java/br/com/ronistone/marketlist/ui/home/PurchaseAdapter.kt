package br.com.ronistone.marketlist.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.databinding.PurchaseItemBinding

class PurchaseAdapter(var items: List<Purchase>): RecyclerView.Adapter<PurchaseAdapter.ViewHolder>(), AdapterItemsContract {


    class ViewHolder(val binding: PurchaseItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(purchase: Purchase) {
            binding.purchaseMarket.text = purchase.market?.name
            val formatter = DateFormat.getLongDateFormat(context)
            val localDate = formatter.format(purchase.createdAt)
            binding.purchaseDate.text = localDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: PurchaseItemBinding = PurchaseItemBinding.inflate(inflater, parent, false)
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
        this.items = items as List<Purchase>
        notifyDataSetChanged()
    }

}
