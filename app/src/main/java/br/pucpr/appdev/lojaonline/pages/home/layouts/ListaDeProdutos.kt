package br.pucpr.appdev.lojaonline.pages.home.layouts

import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.pucpr.appdev.lojaonline.model.Produto
import br.pucpr.appdev.lojaonline.pages.home.HomeViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

@Composable
fun ListaDeProdutos(produtos: List<Produto>, viewModel: HomeViewModel) {
    LazyColumn {
        items(produtos) { produto ->
            ProdutoItem(produto, viewModel)
            Spacer(modifier = Modifier.height(if(produtos.last().id == produto.id) 60.dp else 8.dp))
        }
    }
}

@Composable
fun ProdutoItem(produto: Produto, viewModel: HomeViewModel) {
    val user = viewModel.user.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(produto.link),
            contentDescription = produto.nome,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
            ) {
                Text(text = produto.nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = produto.descricao, fontSize = 14.sp)
            }
            if(user!= null) {
                IconButton(
                    onClick = { viewModel.insertCarrinho(produtoId = produto.id)},
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Adicionar ao Carrinho")
                }
            }
        }
    }
}
