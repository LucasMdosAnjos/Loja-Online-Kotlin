package br.pucpr.appdev.lojaonline.pages.addProduto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.appdev.lojaonline.model.User
import br.pucpr.appdev.lojaonline.pages.register.RegisterState
import br.pucpr.appdev.lojaonline.pages.register.RegisterViewModel
import br.pucpr.appdev.lojaonline.repository.ProdutoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddProdutoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val produtoRepository: ProdutoRepository
) : ViewModel() {
    val userId: Int = savedStateHandle["USER_KEY"]!!
    private val _formState = MutableStateFlow(AddProdutoViewModel.FormState())
    val formState: StateFlow<AddProdutoViewModel.FormState> = _formState

    private val _addProdutoState = MutableStateFlow<AddProdutoState>(AddProdutoState.InitialAddProdutoState)
    val addProdutoState: StateFlow<AddProdutoState> = _addProdutoState

    fun salvarProduto(nome:String,descricao:String,link:String){
        val isNomeValid = nome.length > 3
        val isDescricaoValid = descricao.length > 6
        val isLinkValid = link.isNotEmpty()

        _formState.value = AddProdutoViewModel.FormState(
            isNomeValid = isNomeValid,
            isDescricaoValid = isDescricaoValid,
            isLinkValid = isLinkValid
        )

        viewModelScope.launch {
            try {
                if (isNomeValid && isDescricaoValid && isLinkValid) {
                    _addProdutoState.value = AddProdutoState.LoadingAddProdutoState
                    val success = produtoRepository.insertProduto(nome, descricao, link, userId)

                    if (!success) {
                        _addProdutoState.value = AddProdutoState.ErrorAddProdutoState("Erro ao salvar produto")
                    }else{
                        _addProdutoState.value = AddProdutoState.SuccessAddProdutoState
                    }
                }
            }catch(e:Exception){
                _addProdutoState.value = AddProdutoState.ErrorAddProdutoState("Erro ao salvar produto")
            }
        }
    }

    data class FormState(
        val isNomeValid: Boolean = true,
        val isDescricaoValid: Boolean = true,
        val isLinkValid: Boolean = true
    )
}

sealed class AddProdutoState {
    object InitialAddProdutoState : AddProdutoState()
    object LoadingAddProdutoState : AddProdutoState()
    object SuccessAddProdutoState : AddProdutoState()
    data class ErrorAddProdutoState(val message: String) : AddProdutoState()
}
