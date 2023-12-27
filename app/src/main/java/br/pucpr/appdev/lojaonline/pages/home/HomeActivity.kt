package br.pucpr.appdev.lojaonline.pages.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.pucpr.appdev.lojaonline.pages.addProduto.AddProdutoActivity
import br.pucpr.appdev.lojaonline.pages.home.layouts.CarrinhoDeComprasIconButton
import br.pucpr.appdev.lojaonline.pages.home.layouts.ListaDeProdutos
import br.pucpr.appdev.lojaonline.pages.welcome.WelcomeActivity
import br.pucpr.appdev.lojaonline.ui.theme.LojaOnlineTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaOnlineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProdutos()
        viewModel.getCarrinhos()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val user = viewModel.user.collectAsState().value
    val context = LocalContext.current

    val getProdutosState = viewModel.getProdutosState.collectAsState().value

    val getCarrinhosState = viewModel.getCarrinhosState.collectAsState().value

    val insertCarrinhoState = viewModel.insertCarrinhoState.collectAsState().value

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Loja Online") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF42A5F5), // Um tom de azul suave
                    titleContentColor = Color.White, // Cor do título
                    actionIconContentColor = Color.White // Cor dos ícones de ação),
                ),
                actions = {
                    TextButton(onClick = {
                        context.apply {
                            val intent = Intent(this, WelcomeActivity::class.java)
                            startActivity(intent)
                        }
                    }) {
                        Text(
                            if (user == null) "ENTRAR" else "SAIR",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if(user !=null){
                        getCarrinhosState.apply {
                            val state = this
                            if(state is GetCarrinhosState.SuccessGetCarrinhosState){
                                CarrinhoDeComprasIconButton(
                                    count = state.carrinhos.size,
                                    context=context,
                                    viewModel = viewModel
                                )
                            }
                        }

                    }
                }
            )
        },
        floatingActionButton =
        {
            if (user != null) {
                FloatingActionButton(
                    onClick = {
                        context.apply {
                            val intent = Intent(this, AddProdutoActivity::class.java)
                            intent.putExtra("USER_KEY", user.id)
                            startActivity(intent)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            // Display user info
            Text(
                text = if (user != null) "Olá, ${user.email}" else "Olá, Visitante",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(12.dp))

            getProdutosState.apply {
                val state = this
                if(state is GetProdutosState.SuccessGetProdutosState){
                    if(state.produtos.isNotEmpty()) {
                        val produtos = state.produtos
                        ListaDeProdutos(produtos = produtos, viewModel)
                    }else{
                        Spacer(modifier = Modifier.height(36.dp))
                        Text("Sem produtos cadastrados no momento.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,)
                    }
                }
            }
        }
    }

    getProdutosState.apply {
        val state = this
        if(state is GetProdutosState.ErrorGetProdutosState){
            scope.launch {
                snackbarHostState.showSnackbar(state.message)
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

    insertCarrinhoState.apply {
        val state = this
        if(state is InsertCarrinhoState.ErrorInsertCarrinhoState){
            scope.launch {
                snackbarHostState.showSnackbar(state.message)
            }
        }
    }

}




