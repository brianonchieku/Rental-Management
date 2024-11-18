package com.example.rentals.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentals.model.User
import com.example.rentals.model.UserRepository
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    private val userRepository: UserRepository = UserRepository()
    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: LiveData<List<User>> = _users

    fun fetchUsers(){
        viewModelScope.launch {
            val results = userRepository.fetchUsers()
            _users.value = results
        }
    }

    fun addUser(user: User, context: Context, onComplete:() -> Unit){
        userRepository.addUser(user, context, {
            fetchUsers()
            onComplete()

        },{
            onComplete()
        } )

    }
}