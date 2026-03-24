package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.PloTwistDAO
import org.delcom.dao.RefreshTokenDAO
import org.delcom.dao.UserDAO
import org.delcom.entities.PloTwist
import org.delcom.entities.RefreshToken
import org.delcom.entities.User
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.datetime.Instant

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun userDAOToModel(dao: UserDAO) = User(
    id = dao.id.value.toString(),
    name = dao.name,
    username = dao.username,
    email = dao.email,
    password = dao.password,
    photo = dao.photo,
    about = dao.about,
    location = dao.location,
    link = dao.link,
    createdAt = dao.createdAt,
    updatedAt = dao.updatedAt
)

fun refreshTokenDAOToModel(dao: RefreshTokenDAO) = RefreshToken(
    dao.id.value.toString(),
    dao.userId.toString(),
    dao.refreshToken,
    dao.authToken,
    dao.createdAt,
)

fun plotwistDAOToModel(dao: PloTwistDAO) = PloTwist(
    id = dao.id.value.toString(),
    title = dao.title,
    description = dao.description,
    content = dao.content,
    author = dao.author,
    imagePath = dao.imagePath,
    createdAt = Instant.fromEpochMilliseconds(dao.createdAt),
    updatedAt = Instant.fromEpochMilliseconds(dao.updatedAt)
)
