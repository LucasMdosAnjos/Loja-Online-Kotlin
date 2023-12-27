package br.pucpr.appdev.lojaonline.pages.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.lojaonline.model.User
import br.pucpr.appdev.lojaonline.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.InitialLoginState)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()


    fun login(email: String, password: String) {
        _loginState.value = LoginState.LoadingLoginState
        viewModelScope.launch {
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
                    _loginState.value = LoginState.SuccessLoginState(user)
                } else {
                    _loginState.value =
                        LoginState.ErrorLoginState("Falha no login: usuário não encontrado.")
                }
            } catch (e: Exception) {
                Log.d("testee",e.toString())
                _loginState.value = LoginState.ErrorLoginState("Erro no login")
            }
        }
    }
}
    sealed class LoginState {
        object InitialLoginState : LoginState()
        object LoadingLoginState : LoginState()
        data class SuccessLoginState(val user: User) : LoginState()
        data class ErrorLoginState(val message: String) : LoginState()
    }
