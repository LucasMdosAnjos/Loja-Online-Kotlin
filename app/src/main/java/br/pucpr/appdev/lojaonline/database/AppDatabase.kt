package br.pucpr.appdev.lojaonline.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.pucpr.appdev.lojaonline.model.Carrinho
import br.pucpr.appdev.lojaonline.model.Produto
import br.pucpr.appdev.lojaonline.model.User

@Database(entities = [User::class, Produto::class, Carrinho::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract  fun produtoDao(): ProdutoDao
    abstract fun carrinhoDao(): CarrinhoDao
}