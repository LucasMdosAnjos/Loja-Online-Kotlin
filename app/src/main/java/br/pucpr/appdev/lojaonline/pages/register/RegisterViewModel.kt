package br.pucpr.appdev.lojaonline.pages.register
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.lojaonline.model.User
import br.pucpr.appdev.lojaonline.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.InitialRegisterState)
    val registerState: StateFlow<RegisterState> = _registerState

     fun register(email: String, password: String, confirmPassword: String) {
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length > 6
        val isConfirmPasswordValid = password == confirmPassword

        _formState.value = FormState(
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid,
            isConfirmPasswordValid = isConfirmPasswordValid
        )

        viewModelScope.launch {
            try {
                if (isEmailValid && isPasswordValid && isConfirmPasswordValid) {
                    _registerState.value = RegisterState.LoadingRegisterState
                    val success = userRepository.createUser(email, password)

                    if (!success) {
                        // Tratar o caso de e-mail duplicado
                        _registerState.value = RegisterState.ErrorRegisterState("Usuário com este e-mail já existe.")
                    }else{
                        val user = userRepository.login(email,password)
                        _registerState.value = RegisterState.SuccessRegisterState(user!!)
                    }
                }
            }catch(e:Exception){
                _registerState.value = RegisterState.ErrorRegisterState("Erro ao criar usuário")
            }
        }
    }

    data class FormState(
        val isEmailValid: Boolean = true,
        val isPasswordValid: Boolean = true,
        val isConfirmPasswordValid: Boolean = true
    )

}

sealed class RegisterState {
    object InitialRegisterState : RegisterState()
    object LoadingRegisterState : RegisterState()
    data class SuccessRegisterState(val user: User) : RegisterState()
    data class ErrorRegisterState(val message: String) : RegisterState()
}

