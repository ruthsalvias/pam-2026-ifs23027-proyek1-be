package org.delcom.repositories

import org.delcom.dao.PloTwistDAO
import org.delcom.entities.PloTwist
import org.delcom.helpers.plotwistDAOToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.PloTwistTable
import java.util.*

class PloTwistRepository : IPloTwistRepository {
    override suspend fun getAll(search: String?): List<PloTwist> = suspendTransaction {
        val allPlotWists = PloTwistDAO.all().map(::plotwistDAOToModel).toList()
        
        return@suspendTransaction if (search != null && search.isNotEmpty()) {
            allPlotWists.filter {
                it.title.contains(search, ignoreCase = true) ||
                it.description.contains(search, ignoreCase = true) ||
                it.content.contains(search, ignoreCase = true)
            }
        } else {
            allPlotWists
        }
    }

    override suspend fun getById(id: String): PloTwist? = suspendTransaction {
        try {
            val uuid = UUID.fromString(id)
            PloTwistDAO.findById(uuid)?.let(::plotwistDAOToModel)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override suspend fun create(plotwist: PloTwist, imagePath: String?): String = suspendTransaction {
        val now = System.currentTimeMillis()
        val plotwistDAO = PloTwistDAO.new {
            title = plotwist.title
            description = plotwist.description
            content = plotwist.content
            author = plotwist.author
            this.imagePath = imagePath
            createdAt = now
            updatedAt = now
        }
        plotwistDAO.id.value.toString()
    }

    override suspend fun update(id: String, plotwist: PloTwist, imagePath: String?): Boolean = suspendTransaction {
        try {
            val uuid = UUID.fromString(id)
            val plotwistDAO = PloTwistDAO.findById(uuid) ?: return@suspendTransaction false
            plotwistDAO.apply {
                title = plotwist.title
                description = plotwist.description
                content = plotwist.content
                author = plotwist.author
                if (imagePath != null) {
                    this.imagePath = imagePath
                }
                updatedAt = System.currentTimeMillis()
            }
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override suspend fun delete(id: String): Boolean = suspendTransaction {
        try {
            val uuid = UUID.fromString(id)
            val plotwistDAO = PloTwistDAO.findById(uuid) ?: return@suspendTransaction false
            plotwistDAO.delete()
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
