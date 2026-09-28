package com.example.helper.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toUser() }
        }
    }

    fun searchUsers(query: String): Flow<List<User>> {
        return userDao.searchUsers(query).map { entities ->
            entities.map { it.toUser() }
        }
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)?.toUser()
    }

    suspend fun addUser(name: String, email: String, age: Int) {
        val userEntity = UserEntity(
            name = name,
            email = email,
            age = age
        )
        userDao.insertUser(userEntity)
    }

    suspend fun deleteUser(userId: Int) {
        userDao.deleteUserById(userId)
    }

    suspend fun updateUser(userId: Int, name: String, email: String, age: Int) {
        val userEntity = UserEntity(
            id = userId,
            name = name,
            email = email,
            age = age
        )
        userDao.updateUser(userEntity)
    }

    private fun UserEntity.toUser(): User {
        return User(
            id = id,
            name = name,
            email = email,
            age = age
        )
    }
}
