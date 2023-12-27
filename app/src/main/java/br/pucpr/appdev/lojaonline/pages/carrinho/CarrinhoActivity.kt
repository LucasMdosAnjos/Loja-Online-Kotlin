package br.pucpr.appdev.lojaonline.pages.carrinho

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.pucpr.appdev.lojaonline.pages.carrinho.layouts.ListaDeCarrinhos
import br.pucpr.appdev.lojaonline.pages.home.GetCarrinhosState
import br.pucpr.appdev.lojaonline.pages.home.GetProdutosState
import br.pucpr.appdev.lojaonline.pages.home.layouts.ListaDeProdutos
import br.pucpr.appdev.lojaonline.pages.ui.theme.LojaOnlineTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CarrinhoActivity : ComponentActivity() {
    private val viewModel: CarrinhoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaOnlineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarrinhoScreen(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrinhoScreen(viewModel: CarrinhoViewModel) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val getCarrinhosState = viewModel.getCarrinhosState.collectAsState().value
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Carrinho de Compras") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF42A5F5), // Um tom de azul suave
                    titleContentColor = Color.White, // Cor do título
                    actionIconContentColor = Color.White // Cor dos ícones de ação),
                ),
                navigationIcon = {
                    IconButton(onClick = {

                        (context as? ComponentActivity)?.onBackPressedDispatcher!!.onBackPressed()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            getCarrinhosState.apply {
                val state = this
                if(state is GetCarrinhosState.SuccessGetCarrinhosState){
                    if(state.carrinhos.isNotEmpty()) {
                        val carrinhos = state.carrinhos
                        ListaDeCarrinhos(carrinhos = carrinhos, viewModel)
                    }else{
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            Spacer(modifier = Modifier.height(34.dp))
                            Text(
                                "Você ainda não tem produtos no carrinho",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                    }
                }
            }
        }
    }

    getCarrinhosState.apply {
        val state = this
        if(state is GetCarrinhosState.ErrorGetCarrinhosState){
            scope.launch {
                snackbarHostState.showSnackbar(state.message)
            }
        }
    }
}


