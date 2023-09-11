package br.com.ronistone.marketlist.model

import java.util.Date

data class User(
    val id: Int,
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
