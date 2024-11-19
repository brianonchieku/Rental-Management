package com.example.rentals.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentals.LoginState
import com.example.rentals.model.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val authRepository: AuthRepository = AuthRepository()

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String){
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val results = authRepository.loginUser(email, password)
            if (results.isSuccess){
                _loginState.value = LoginState.Success
            }else{
                _loginState.value = LoginState.Error(results.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}