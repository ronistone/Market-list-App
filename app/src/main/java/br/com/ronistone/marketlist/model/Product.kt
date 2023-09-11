package br.com.ronistone.marketlist.model

import java.util.Date

data class Product(
    val id: Int,
    val ean: String?,
    val name: String,
    val unit: String,
    val size: Int,
    val createdAt: Date,
    val updatedAt: Date
) {
    override fun toString(): String {
        return "Product(id=$id, ean=$ean, name='$name', unit='$unit', size=$size, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
