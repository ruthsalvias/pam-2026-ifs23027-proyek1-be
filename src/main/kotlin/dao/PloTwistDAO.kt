package org.delcom.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.delcom.tables.PloTwistTable
import java.util.*

class PloTwistDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PloTwistDAO>(PloTwistTable)

    var title by PloTwistTable.title
    var description by PloTwistTable.description
    var content by PloTwistTable.content
    var author by PloTwistTable.author
    var imagePath by PloTwistTable.imagePath
    var createdAt by PloTwistTable.createdAt
    var updatedAt by PloTwistTable.updatedAt
}
