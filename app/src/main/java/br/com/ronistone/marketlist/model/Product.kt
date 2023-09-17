package br.com.ronistone.marketlist.model

import java.util.Date

data class Product @JvmOverloads constructor(
    val id: Int? = null,
    val ean: String? = null,
    val name: String,
    val unit: String,
    val size: Int,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    override fun toString(): String {
        return "$name $size $unit"
    }
}
