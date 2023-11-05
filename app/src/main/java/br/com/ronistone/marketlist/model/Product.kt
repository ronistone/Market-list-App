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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (ean != other.ean) return false
        if (name != other.name) return false
        if (unit != other.unit) return false
        if (size != other.size) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (ean?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + (size ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        return result
    }


}
