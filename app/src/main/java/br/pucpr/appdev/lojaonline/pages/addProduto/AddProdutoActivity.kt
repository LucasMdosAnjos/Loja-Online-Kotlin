package br.pucpr.appdev.lojaonline.pages.addProduto

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.lojaonline.ui.theme.LojaOnlineTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import br.pucpr.appdev.lojaonline.pages.home.HomeActivity
import br.pucpr.appdev.lojaonline.pages.login.LoginState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProdutoActivity : ComponentActivity() {
    private val viewModel: AddProdutoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaOnlineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddProdutoScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProdutoScreen(viewModel: AddProdutoViewModel) {
    val context = LocalContext.current
    var nome by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    val formState = viewModel.formState.collectAsState().value

    val addProdutoState = viewModel.addProdutoState.collectAsState().value

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Produto") },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.7f)
                    .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = {nome = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Nome do Produto") },
                    singleLine = true,  // Impede múltiplas linhas
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    isError = !formState.isNomeValid
                )

                OutlinedTextField(
                    value = descricao,
                    onValueChange = {descricao = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Descrição") },

                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    isError = !formState.isDescricaoValid
                )

                // Campo link produto
                OutlinedTextField(
                    value = link,
                    onValueChange = {link = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text("Link do Produto (Foto)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    isError = !formState.isLinkValid
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.3f)
                    .padding(12.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = { viewModel.salvarProduto(nome, descricao, link)},
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Salvar Produto")
                }
            }
        }
    }

    addProdutoState.apply {
        if (addProdutoState is AddProdutoState.ErrorAddProdutoState){
            scope.launch {
                snackbarHostState.showSnackbar(addProdutoState.message)
            }
        }

        if(addProdutoState is AddProdutoState.SuccessAddProdutoState){
            context.apply {
                (context as? ComponentActivity)?.onBackPressedDispatcher!!.onBackPressed()
            }
        }
    }
}
