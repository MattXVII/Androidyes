package it.univaq.jsonplaceholder.data.local

import it.univaq.jsonplaceholder.data.local.entities.UserEntity
import it.univaq.jsonplaceholder.domain.model.User
import it.univaq.jsonplaceholder.domain.repositories.LocalRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

private fun User.toEntity() = UserEntity(
    name = name,
    username = username,
    email = email,
    city = city,
    lat = lat,
    lng = lng
)

private fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    username = username,
    email = email,
    city = city,
    lat = lat,
    lng = lng
)

class RoomLocalRepository @Inject constructor(
    private val userDao: UserDao
): LocalRepository {

    override suspend fun save(data: List<User>) {
        with(Dispatchers.IO) {
            clear()
            userDao.insert(data.map { it.toEntity() })
        }
    }

    override suspend fun getAll(): List<User> {
        return userDao.getAll().map { it.toDomain() }
    }

    override suspend fun clear() {
        userDao.clear()
    }
}