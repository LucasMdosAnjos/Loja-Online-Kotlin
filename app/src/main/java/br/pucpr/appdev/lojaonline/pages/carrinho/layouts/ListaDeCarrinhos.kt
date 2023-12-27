package br.pucpr.appdev.lojaonline.pages.carrinho.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import br.pucpr.appdev.lojaonline.model.CarrinhoComProduto
import br.pucpr.appdev.lojaonline.model.Produto
import br.pucpr.appdev.lojaonline.pages.carrinho.CarrinhoViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun ListaDeCarrinhos(carrinhos: List<CarrinhoComProduto>, viewModel: CarrinhoViewModel) {
    LazyColumn {
        items(carrinhos) { carrinhoComProduto ->
            ProdutoItem(carrinhoComProduto.produto,carrinhoComProduto.carrinho.id, viewModel)
            Spacer(modifier = Modifier.height(if(carrinhos.last().carrinho.id == carrinhoComProduto.carrinho.id) 60.dp else 8.dp))
        }
    }
}

@Composable
    fun ProdutoItem(produto: Produto,carrinhoId: Int, viewModel: CarrinhoViewModel) {
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
                    onClick = {viewModel.deleteCarrinho(carrinhoId = carrinhoId) },
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Adicionar ao Carrinho")
                }
            }
        }
    }
}