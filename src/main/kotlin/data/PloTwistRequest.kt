package org.delcom.data

import kotlinx.serialization.Serializable

@Serializable
data class PloTwistRequest(
    val title: String = "",
    val description: String = "",
    val content: String = "",
    val author: String = ""
)
