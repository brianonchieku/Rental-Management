package com.example.rentals.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentals.NetworkResponse
import com.example.rentals.model.User
import com.example.rentals.model.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    private val userRepository: UserRepository = UserRepository()
    private val _users = MutableLiveData<NetworkResponse<List<User>>>()
    val users: LiveData<NetworkResponse<List<User>>> = _users

    fun fetchUsers(){

        _users.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val results = userRepository.fetchUsers()
                results.let {
                    _users.value = NetworkResponse.Success(it)
                }
            } catch (e: FirebaseFirestoreException){
                _users.value = NetworkResponse.Error("failed to load data")
            }

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