package com.example.helper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.helper.data.AppDatabase
import com.example.helper.data.User
import com.example.helper.data.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository by lazy {
        val database = AppDatabase.getDatabase(application)
        UserRepository(database.userDao())
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val users: StateFlow<List<User>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllUsers()
            } else {
                repository.searchUsers(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addUser(name: String, email: String, age: Int) {
        viewModelScope.launch {
            repository.addUser(name, email, age)
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            repository.deleteUser(userId)
        }
    }

    fun updateUser(userId: Int, name: String, email: String, age: Int) {
        viewModelScope.launch {
            repository.updateUser(userId, name, email, age)
        }
    }
}
