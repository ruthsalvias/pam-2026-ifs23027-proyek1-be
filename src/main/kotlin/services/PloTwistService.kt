package org.delcom.services

import io.ktor.http.content.PartData
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.delcom.data.DataResponse
import org.delcom.entities.PloTwist
import org.delcom.repositories.IPloTwistRepository
import java.io.File
import java.util.*

typealias ApiResponse = DataResponse<Map<String, Any?>>

class PloTwistService(
    private val plotwistRepository: IPloTwistRepository,
    private val uploadsDir: String = "uploads/plotwist"
) {
    init {
        File(uploadsDir).mkdirs()
    }

    suspend fun getAll(call: RoutingCall) {
        try {
            val search = call.parameters["search"]
            val plotwists = plotwistRepository.getAll(search)
            call.respond<ApiResponse>(
                DataResponse(
                    status = "success",
                    message = "PloTwists retrieved successfully",
                    data = mapOf("ploTwists" to plotwists)
                )
            )
        } catch (e: Exception) {
            call.respond<ApiResponse>(
                DataResponse(
                    status = "error",
                    message = e.message ?: "Failed to retrieve PloTwists"
                )
            )
        }
    }

    suspend fun getById(call: RoutingCall) {
        try {
            val id = call.parameters["id"] ?: return
            val plotwist = plotwistRepository.getById(id)
            if (plotwist == null) {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "PloTwist not found"
                    )
                )
                return
            }
            call.respond<ApiResponse>(
                DataResponse(
                    status = "success",
                    message = "PloTwist retrieved successfully",
                    data = mapOf("ploTwist" to plotwist)
                )
            )
        } catch (e: Exception) {
            call.respond<ApiResponse>(
                DataResponse(
                    status = "error",
                    message = e.message ?: "Failed to retrieve PloTwist"
                )
            )
        }
    }

    suspend fun create(call: RoutingCall) {
        try {
            var title = ""
            var description = ""
            var content = ""
            var author = ""
            var imagePath: String? = null

            val multipartData = call.receiveMultipart()
            while (true) {
                val part = multipartData.readPart() ?: break
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "title" -> title = part.value
                            "description" -> description = part.value
                            "content" -> content = part.value
                            "author" -> author = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "file") {
                            val ext = part.originalFileName
                                ?.substringAfterLast('.', "")
                                ?.let { if (it.isNotEmpty()) ".$it" else "" }
                                ?: ""

                            val fileName = UUID.randomUUID().toString() + ext
                            val filePath = "$uploadsDir/$fileName"

                            withContext(Dispatchers.IO) {
                                val file = File(filePath)
                                file.parentFile?.mkdirs()
                                part.provider().copyAndClose(file.writeChannel())
                                imagePath = filePath
                            }
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (title.isBlank() || description.isBlank() || content.isBlank() || author.isBlank()) {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "All fields are required"
                    )
                )
                return
            }

            val plotwist = PloTwist(
                title = title,
                description = description,
                content = content,
                author = author,
                createdAt = kotlinx.datetime.Clock.System.now(),
                updatedAt = kotlinx.datetime.Clock.System.now()
            )

            val plotwistId = plotwistRepository.create(plotwist, imagePath)
            call.respond<ApiResponse>(
                DataResponse(
                    status = "success",
                    message = "PloTwist created successfully",
                    data = mapOf("ploTwistId" to plotwistId)
                )
            )
        } catch (e: Exception) {
            call.respond<ApiResponse>(
                DataResponse(
                    status = "error",
                    message = e.message ?: "Failed to create PloTwist"
                )
            )
        }
    }

    suspend fun update(call: RoutingCall) {
        try {
            val id = call.parameters["id"] ?: return
            val existingPloTwist = plotwistRepository.getById(id)
            if (existingPloTwist == null) {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "PloTwist not found"
                    )
                )
                return
            }

            var title = existingPloTwist.title
            var description = existingPloTwist.description
            var content = existingPloTwist.content
            var author = existingPloTwist.author
            var imagePath: String? = null

            val multipartData = call.receiveMultipart()
            while (true) {
                val part = multipartData.readPart() ?: break
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "title" -> title = part.value
                            "description" -> description = part.value
                            "content" -> content = part.value
                            "author" -> author = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "file") {
                            val ext = part.originalFileName
                                ?.substringAfterLast('.', "")
                                ?.let { if (it.isNotEmpty()) ".$it" else "" }
                                ?: ""

                            val fileName = UUID.randomUUID().toString() + ext
                            val filePath = "$uploadsDir/$fileName"

                            withContext(Dispatchers.IO) {
                                val file = File(filePath)
                                file.parentFile?.mkdirs()
                                part.provider().copyAndClose(file.writeChannel())
                                imagePath = filePath
                            }
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            val plotwist = PloTwist(
                id = id,
                title = title,
                description = description,
                content = content,
                author = author,
                createdAt = existingPloTwist.createdAt,
                updatedAt = kotlinx.datetime.Clock.System.now()
            )

            val success = plotwistRepository.update(id, plotwist, imagePath)
            if (success) {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "success",
                        message = "PloTwist updated successfully"
                    )
                )
            } else {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "Failed to update PloTwist"
                    )
                )
            }
        } catch (e: Exception) {
            call.respond<ApiResponse>(
                DataResponse(
                    status = "error",
                    message = e.message ?: "Failed to update PloTwist"
                )
            )
        }
    }

    suspend fun delete(call: RoutingCall) {
        try {
            val id = call.parameters["id"] ?: return
            val success = plotwistRepository.delete(id)
            if (success) {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "success",
                        message = "PloTwist deleted successfully"
                    )
                )
            } else {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "PloTwist not found"
                    )
                )
            }
        } catch (e: Exception) {
            call.respond<ApiResponse>(
                DataResponse(
                    status = "error",
                    message = e.message ?: "Failed to delete PloTwist"
                )
            )
        }
    }

    suspend fun getImage(call: RoutingCall) {
        try {
            val id = call.parameters["id"] ?: return
            val plotwist = plotwistRepository.getById(id)
            if (plotwist?.imagePath == null) {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "Image not found"
                    )
                )
                return
            }
            val file = File(uploadsDir, plotwist.imagePath!!.substringAfterLast("/"))
            if (file.exists()) {
                call.respond(file.readBytes())
            } else {
                call.respond<ApiResponse>(
                    DataResponse(
                        status = "error",
                        message = "Image file not found"
                    )
                )
            }
        } catch (e: Exception) {
            call.respond<ApiResponse>(
                DataResponse(
                    status = "error",
                    message = e.message ?: "Failed to retrieve image"
                )
            )
        }
    }
}
