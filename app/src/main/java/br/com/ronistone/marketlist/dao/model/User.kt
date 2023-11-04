package br.com.ronistone.marketlist.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class User(
    @PrimaryKey val id: Int,
    val email: String,
    val name: String,
    val password: String?,
    val createdAt: Date,
    val updatedAt: Date?,
) {
    override fun toString(): String {
        return "User(id=$id, email='$email', name='$name', password=$password, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
