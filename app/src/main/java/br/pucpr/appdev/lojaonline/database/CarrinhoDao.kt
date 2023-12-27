package br.pucpr.appdev.lojaonline.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.pucpr.appdev.lojaonline.model.Carrinho
import br.pucpr.appdev.lojaonline.model.CarrinhoComProduto

@Dao
interface CarrinhoDao {
    // Método para inserir um carrinho
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCarrinho(carrinho: Carrinho): Long

    @Query("SELECT * FROM carrinho WHERE userId=:userId ORDER BY id DESC")
    suspend fun getCarrinhos(userId: Int): List<CarrinhoComProduto>

    @Query("DELETE FROM carrinho WHERE id = :carrinhoId")
    suspend fun deleteCarrinho(carrinhoId: Int): Int
}