package br.pucpr.appdev.lojaonline.pages.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WelcomeViewModel @Inject constructor(
) : ViewModel() {

    init {
        viewModelScope.launch {
            // Inicialize ou faça chamadas de rede/repositório aqui, se necessário
        }
    }


}
