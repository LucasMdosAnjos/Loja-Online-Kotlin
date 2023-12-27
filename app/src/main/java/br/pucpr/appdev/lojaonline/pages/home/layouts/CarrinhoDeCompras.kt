package br.pucpr.appdev.lojaonline.pages.home.layouts

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import br.pucpr.appdev.lojaonline.pages.addProduto.AddProdutoActivity
import br.pucpr.appdev.lojaonline.pages.carrinho.CarrinhoActivity
import br.pucpr.appdev.lojaonline.pages.home.HomeViewModel

@Composable
fun CarrinhoDeComprasIconButton(count: Int, context: Context, viewModel: HomeViewModel) {
    Box(contentAlignment = Alignment.Center) {
        IconButton(onClick = {
            val user = viewModel.user.value
            if(user!=null){
            context.apply {
                val intent = Intent(this, CarrinhoActivity::class.java)
                intent.putExtra("USER_KEY", user.id)
                startActivity(intent)
            }
            }
        }) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrinho")
        }

        // Contador de produtos
        if (count > 0) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset(x = 12.dp, y = (-12).dp)
                    .size(18.dp)
                    .background(Color.Red, shape = androidx.compose.foundation.shape.CircleShape)
            ) {
                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}
