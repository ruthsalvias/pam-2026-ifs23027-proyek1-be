package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object PloTwistTable : UUIDTable("plotwists") {
    val title = varchar("title", length = 150)
    val description = text("description")
    val content = text("content")
    val author = varchar("author", length = 100)
    val imagePath = varchar("image_path", length = 255).nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}
