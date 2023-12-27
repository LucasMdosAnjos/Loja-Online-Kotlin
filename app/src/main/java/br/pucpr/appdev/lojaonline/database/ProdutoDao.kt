package br.pucpr.appdev.lojaonline.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.pucpr.appdev.lojaonline.model.Produto

@Dao
interface ProdutoDao {
    // Método para inserir um produto
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduto(produto: Produto): Long

    @Query("SELECT * FROM produto ORDER BY id DESC")
    suspend fun getProdutos(): List<Produto>
}