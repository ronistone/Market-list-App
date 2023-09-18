package br.com.ronistone.marketlist.model

import java.util.Date

data class Product @JvmOverloads constructor(
    val id: Int? = null,
    val ean: String? = null,
    val name: String? = null,
    val unit: String? = null,
    val size: Int? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    override fun toString(): String {
        return "$name $size $unit"
    }
}
