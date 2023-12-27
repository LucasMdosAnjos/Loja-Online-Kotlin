package br.pucpr.appdev.lojaonline.repository



import br.pucpr.appdev.lojaonline.database.ProdutoDao
import br.pucpr.appdev.lojaonline.model.Produto
import javax.inject.Inject

class ProdutoRepository @Inject constructor(private val produtoDao: ProdutoDao) {
    suspend fun insertProduto(nome: String, descricao: String, link: String, userId: Int): Boolean {

        val newProduto = Produto(nome = nome, descricao = descricao, link = link, userId = userId)
        val rowId = produtoDao.insertProduto(newProduto)
        return rowId != -1L  // Retorna true se o produto foi inserido com sucesso
    }

    suspend fun getProdutos(): List<Produto> {

        val produtos = produtoDao.getProdutos()
        return produtos
    }
}