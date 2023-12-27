package br.pucpr.appdev.lojaonline.pages.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import br.pucpr.appdev.lojaonline.ui.theme.LojaOnlineTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.lojaonline.pages.home.HomeActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaOnlineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen(viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterScreen(viewModel: RegisterViewModel) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        var confirmPasswordVisibility by remember { mutableStateOf(false) }

        val registerState = viewModel.registerState.collectAsState().value

        val formState = viewModel.formState.collectAsState().value
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                TopAppBar(
                    title = { Text("Registrar") },
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
                    .fillMaxSize(),
            ) {
                // Campos de texto e botão de registro
                Column(
                    modifier = Modifier
                        .fillMaxSize().weight(0.7f)
                        .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        isError = !formState.isEmailValid,
                        singleLine = true,  // Impede múltiplas linhas
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    // Campo de senha
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        singleLine = true,
                        isError = !formState.isPasswordValid,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(image, "Toggle password visibility")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    // Campo confirmar senha
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar Senha") },
                        singleLine = true,
                        isError = !formState.isConfirmPasswordValid,
                        visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = {
                                confirmPasswordVisibility = !confirmPasswordVisibility
                            }) {
                                Icon(image, "Toggle password visibility")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize().weight(0.3f)
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = { viewModel.register(email, password, confirmPassword) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Registrar")
                    }
                }
            }
        }

        // Verifica se há mensagem de erro
        registerState?.let {
            if(registerState is RegisterState.ErrorRegisterState) {
                scope.launch {
                    snackbarHostState.showSnackbar(registerState.message)
                }
            }

            if(registerState is RegisterState.SuccessRegisterState){
                context.apply {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("USER_KEY", registerState.user.id)
                    startActivity(intent)
                }
            }
        }

    }
}
