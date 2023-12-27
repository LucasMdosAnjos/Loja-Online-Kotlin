package br.pucpr.appdev.lojaonline.repository

import br.pucpr.appdev.lojaonline.database.CarrinhoDao
import br.pucpr.appdev.lojaonline.database.ProdutoDao
import br.pucpr.appdev.lojaonline.model.Carrinho
import br.pucpr.appdev.lojaonline.model.CarrinhoComProduto
import br.pucpr.appdev.lojaonline.model.Produto
import javax.inject.Inject

class CarrinhoRepository @Inject constructor(private val carrinhoDao: CarrinhoDao) {
    suspend fun insertCarrinho(userId: Int, produtoId: Int): Boolean {

        val newCarrinho = Carrinho(userId = userId, produtoId = produtoId)
        val rowId = carrinhoDao.insertCarrinho(newCarrinho)
        return rowId != -1L
    }

    suspend fun getCarrinhos(userId: Int): List<CarrinhoComProduto> {

        val carrinhos = carrinhoDao.getCarrinhos(userId = userId)
        return carrinhos
    }

    suspend fun deleteCarrinho(carrinhoId: Int): Boolean{
        val rowsAffected = carrinhoDao.deleteCarrinho(carrinhoId = carrinhoId)
        return rowsAffected > 0
    }
}