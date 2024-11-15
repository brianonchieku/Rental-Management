package com.example.rentals.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentals.model.User
import com.example.rentals.model.UserRepository
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    val userRepository: UserRepository = UserRepository()
    private val _users = MutableLiveData<User>()
    val users: LiveData<User> = _users

    fun fetchUsers(){
        viewModelScope.launch {
            val results = userRepository.fetchUsers()
        }


    }
}