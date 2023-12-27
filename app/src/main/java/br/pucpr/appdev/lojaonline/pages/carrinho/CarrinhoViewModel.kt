package br.pucpr.appdev.lojaonline.pages.carrinho

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.lojaonline.model.User
import br.pucpr.appdev.lojaonline.pages.home.GetCarrinhosState
import br.pucpr.appdev.lojaonline.repository.CarrinhoRepository
import br.pucpr.appdev.lojaonline.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarrinhoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val carrinhoRepository: CarrinhoRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _getCarrinhosState = MutableStateFlow<GetCarrinhosState>(GetCarrinhosState.InitialGetCarrinhosState)
    val getCarrinhosState: StateFlow<GetCarrinhosState> = _getCarrinhosState

    private val _deleteCarrinhoState = MutableStateFlow<DeleteCarrinhoState>(DeleteCarrinhoState.InitialDeleteCarrinhoState)
    val deleteCarrinhoState: StateFlow<DeleteCarrinhoState> = _deleteCarrinhoState

    init {
        val userId: Int? = savedStateHandle["USER_KEY"]
        viewModelScope.launch {
            if(userId != null) {
                _user.value = userRepository.getUserById(userId)
            }
            getCarrinhos()
        }
    }
    fun getCarrinhos(){
        viewModelScope.launch {
            val user = user.value
            if(user!= null) {
                try {
                    _getCarrinhosState.value = GetCarrinhosState.LoadingGetCarrinhosState
                    val carrinhos = carrinhoRepository.getCarrinhos(user.id)
                    _getCarrinhosState.value = GetCarrinhosState.SuccessGetCarrinhosState(carrinhos)
                } catch (e: Exception) {
                    _getCarrinhosState.value =
                        GetCarrinhosState.ErrorGetCarrinhosState("Erro ao carregar o carrinho de compras")
                }
            }
        }
    }

    fun deleteCarrinho(carrinhoId: Int){
        viewModelScope.launch {
            try {
                _deleteCarrinhoState.value = DeleteCarrinhoState.LoadingDeleteCarrinhoState
                val success = carrinhoRepository.deleteCarrinho(carrinhoId = carrinhoId)
                if(success) {
                    _deleteCarrinhoState.value = DeleteCarrinhoState.SuccessDeleteCarrinhoState
                    getCarrinhos()
                }else{
                    _deleteCarrinhoState.value =
                        DeleteCarrinhoState.ErrorDeleteCarrinhoState("Erro ao excluir produto do carrinho de compras")
                }
            } catch (e: Exception) {
                _deleteCarrinhoState.value =
                    DeleteCarrinhoState.ErrorDeleteCarrinhoState("Erro ao excluir produto do carrinho de compras")
            }
        }
    }
}

sealed class DeleteCarrinhoState{
    object InitialDeleteCarrinhoState: DeleteCarrinhoState()
    object LoadingDeleteCarrinhoState: DeleteCarrinhoState()
    object SuccessDeleteCarrinhoState: DeleteCarrinhoState()
    data class ErrorDeleteCarrinhoState(val message: String): DeleteCarrinhoState()
}