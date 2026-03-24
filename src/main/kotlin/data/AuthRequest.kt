package org.delcom.data

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.delcom.entities.User

@Serializable
data class AuthRequest(
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var newPassword: String = "",
    var about: String? = null,
    var location: String? = null,
    var link: String? = null,
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
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
        return User(
            name = name,
            username = username,
            email = email,
            password = password,
            about = about,
            location = location,
            link = link,
            updatedAt = Clock.System.now()
        )
    }

}