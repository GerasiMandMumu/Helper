package com.example.helper.viewmodel

import androidx.lifecycle.ViewModel
import com.example.helper.data.UserRepository
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    val users: StateFlow<List<User>> = repository.users

    fun addUser(name: String, email: String, age: Int) {
        repository.addUser(name, email, age)
    }

    fun deleteUser(userId: Int) {
        repository.deleteUser(userId)
    }

    fun updateUser(userId: Int, name: String, email: String, age: Int) {
        repository.updateUser(userId, name, email, age)
    }
}
