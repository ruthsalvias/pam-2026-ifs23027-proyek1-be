package org.delcom.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    var id : String = UUID.randomUUID().toString(),
    var fullName: String,
    var username: String,
    var email: String,
    var password: String,
    var photo: String? = null,
    var about: String? = null,
    var location: String? = null,
    var link: String? = null,

    @Contextual
    val createdAt: Instant = Clock.System.now(),
    @Contextual
    var updatedAt: Instant = Clock.System.now(),
)