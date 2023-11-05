package br.com.ronistone.marketlist.adapter

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ItemKeyProvider(
    private val adapter: ListAdapter<ItemHolder, *>
): ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String =
        adapter.currentList[position].id().toString()

    override fun getPosition(key: String): Int =
        adapter.currentList.indexOfFirst { it.id().toString() == key }
}

class ItemComparator<T : ItemHolder> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id() == newItem.id()
    }
}
