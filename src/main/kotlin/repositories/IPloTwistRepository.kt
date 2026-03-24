package org.delcom.repositories

import org.delcom.entities.PloTwist

interface IPloTwistRepository {
    suspend fun getAll(search: String? = null): List<PloTwist>
    suspend fun getById(id: String): PloTwist?
    suspend fun create(plotwist: PloTwist, imagePath: String?): String
    suspend fun update(id: String, plotwist: PloTwist, imagePath: String?): Boolean
    suspend fun delete(id: String): Boolean
}
