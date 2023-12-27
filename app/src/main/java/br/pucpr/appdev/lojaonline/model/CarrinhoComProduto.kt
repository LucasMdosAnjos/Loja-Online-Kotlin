package br.pucpr.appdev.lojaonline.model

import androidx.room.Embedded
import androidx.room.Relation

data class CarrinhoComProduto(
    @Embedded val carrinho: Carrinho,
    @Relation(
        parentColumn = "produtoId",
        entityColumn = "id"
    )
    val produto: Produto
)