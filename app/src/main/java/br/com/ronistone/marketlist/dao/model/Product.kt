package br.com.ronistone.marketlist.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
class Product(

    @PrimaryKey
    val id: Int? = null,
    val ean: String? = null,
    val name: String? = null,
    val unit: String? = null,
    val size: Int? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)  {
}
