package org.delcom.data

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.delcom.entities.User

@Serializable
data class AuthRequest(
    var name: String = "",
    var fullName: String = "",  // Tambahan untuk kompatibilitas frontend
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var newPassword: String = "",
    var about: String? = null,
    var location: String? = null,
    var link: String? = null,
){
    fun toMap(): Map<String, Any?> {
        // Gunakan fullName jika name kosong
        val finalName = if (name.isNotBlank()) name else fullName
        return mapOf(
            "name" to finalName,
            "username" to username,
            "email" to email,
            "password" to password,
            "newPassword" to newPassword,
            "about" to about,
            "location" to location,
            "link" to link,
        )
    }

    fun toEntity(): User {
        // Gunakan fullName jika name kosong
        val finalName = if (name.isNotBlank()) name else fullName
        return User(
            name = finalName,
            username = username,
            email = email,
            password = password,
            about = about,
            location = location,
            link = link,
            updatedAt = kotlinx.datetime.Clock.System.now()
        )
    }

}