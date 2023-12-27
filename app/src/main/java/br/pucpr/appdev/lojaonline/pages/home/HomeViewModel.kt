package br.pucpr.appdev.lojaonline.pages.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.lojaonline.model.CarrinhoComProduto
import br.pucpr.appdev.lojaonline.model.Produto
import br.pucpr.appdev.lojaonline.model.User
import br.pucpr.appdev.lojaonline.repository.CarrinhoRepository
import br.pucpr.appdev.lojaonline.repository.ProdutoRepository
import br.pucpr.appdev.lojaonline.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val produtoRepository: ProdutoRepository,
    private val carrinhoRepository: CarrinhoRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _getProdutosState = MutableStateFlow<GetProdutosState>(GetProdutosState.InitialGetProdutosState)
    val getProdutosState: StateFlow<GetProdutosState> = _getProdutosState

    private val _getCarrinhosState = MutableStateFlow<GetCarrinhosState>(GetCarrinhosState.InitialGetCarrinhosState)
    val getCarrinhosState: StateFlow<GetCarrinhosState> = _getCarrinhosState

    private val _insertCarrinhoState = MutableStateFlow<InsertCarrinhoState>(InsertCarrinhoState.InitialInsertCarrinhoState)
    val insertCarrinhoState: StateFlow<InsertCarrinhoState> = _insertCarrinhoState
    init {
        val userId: Int? = savedStateHandle["USER_KEY"]
        viewModelScope.launch {
            if(userId != null) {
                _user.value = userRepository.getUserById(userId)
            }
            getProdutos()
            getCarrinhos()
        }
    }

    fun getProdutos(){
        viewModelScope.launch{
            try {
                _getProdutosState.value = GetProdutosState.LoadingGetProdutosState
                val produtos = produtoRepository.getProdutos()
                _getProdutosState.value = GetProdutosState.SuccessGetProdutosState(produtos)
            }catch(e:Exception){
                _getProdutosState.value = GetProdutosState.ErrorGetProdutosState("Erro ao carregar os produtos")
            }
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

    fun insertCarrinho(produtoId: Int){
        viewModelScope.launch {
            val user = user.value
            if(user!= null) {
                try {
                    _insertCarrinhoState.value = InsertCarrinhoState.LoadingInsertCarrinhoState
                    val success = carrinhoRepository.insertCarrinho(userId = user.id, produtoId = produtoId)
                    if(success) {
                        _insertCarrinhoState.value = InsertCarrinhoState.SuccessInsertCarrinhoState
                        getCarrinhos()
                    }else{
                        _insertCarrinhoState.value =
                            InsertCarrinhoState.ErrorInsertCarrinhoState("Erro ao colocar produto no carrinho de compras")
                    }
                } catch (e: Exception) {
                    _insertCarrinhoState.value =
                        InsertCarrinhoState.ErrorInsertCarrinhoState("Erro ao colocar produto no carrinho de compras")
                }
            }
        }
    }
}

sealed class GetProdutosState{
    object InitialGetProdutosState : GetProdutosState()
    object LoadingGetProdutosState: GetProdutosState()
    data class SuccessGetProdutosState(val produtos:List<Produto>) : GetProdutosState()
    data class ErrorGetProdutosState(val message: String) : GetProdutosState()
}

sealed class GetCarrinhosState{
    object InitialGetCarrinhosState : GetCarrinhosState()
    object LoadingGetCarrinhosState: GetCarrinhosState()
    data class SuccessGetCarrinhosState(val carrinhos:List<CarrinhoComProduto>) : GetCarrinhosState()
    data class ErrorGetCarrinhosState(val message: String) : GetCarrinhosState()
}

sealed class InsertCarrinhoState{
    object InitialInsertCarrinhoState : InsertCarrinhoState()
    object LoadingInsertCarrinhoState: InsertCarrinhoState()
    object SuccessInsertCarrinhoState : InsertCarrinhoState()
    data class ErrorInsertCarrinhoState(val message: String) : InsertCarrinhoState()
}