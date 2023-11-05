package br.com.ronistone.marketlist.adapter

import androidx.recyclerview.selection.ItemDetailsLookup

interface ItemHolder {
    fun id(): Int
}

interface ItemHolderDetails {
    fun getItemDetails(): ItemDetailsLookup.ItemDetails<String>
}
