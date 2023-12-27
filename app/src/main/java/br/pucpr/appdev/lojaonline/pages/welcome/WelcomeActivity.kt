package br.pucpr.appdev.lojaonline.pages.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.pucpr.appdev.lojaonline.ui.theme.LojaOnlineTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.platform.LocalContext
import br.pucpr.appdev.lojaonline.pages.login.LoginActivity
import br.pucpr.appdev.lojaonline.pages.register.RegisterActivity

@AndroidEntryPoint
class WelcomeActivity : ComponentActivity() {
    private val viewModel: WelcomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LojaOnlineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WelcomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loja Online") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF42A5F5), // Um tom de azul suave
                    titleContentColor = Color.White, // Cor do título
                    actionIconContentColor = Color.White // Cor dos ícones de ação),
            ))
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()

            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Botão de Login
                    Button(
                        onClick = {navigateToLoginActivity(context)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .height(50.dp)
                    ) {
                        Text("Login")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão de Cadastro
                    Button(
                        onClick = { navigateToRegisterActivity(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cadastro")
                    }
                }
            }
        }
    }
}

fun navigateToRegisterActivity(context: Context) {
    context.apply {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}

fun navigateToLoginActivity(context: Context) {
    context.apply {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}