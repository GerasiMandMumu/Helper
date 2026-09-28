package com.example.helper.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepository {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private var nextId = 1

    fun addUser(name: String, email: String, age: Int) {
        val newUser = User(
            id = nextId++,
            name = name,
            email = email,
            age = age
        )
        _users.value += newUser
    }

    fun deleteUser(userId: Int) {
        _users.value = _users.value.filter { it.id != userId }
    }

    fun getUserById(userId: Int): User? {
        return _users.value.find { it.id == userId }
    }

    fun updateUser(userId: Int, name: String, email: String, age: Int) {
        _users.value = _users.value.map { user ->
            if (user.id == userId) {
                user.copy(name = name, email = email, age = age)
            } else {
                user
            }
        }
    }
}
