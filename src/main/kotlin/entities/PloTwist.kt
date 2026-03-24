package org.delcom.entities

import kotlinx.datetime.Instant

data class PloTwist(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val content: String = "",
    val author: String = "",
    val imagePath: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)
