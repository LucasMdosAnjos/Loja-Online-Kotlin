package br.pucpr.appdev.lojaonline.di

import android.content.Context
import androidx.room.Room
import br.pucpr.appdev.lojaonline.database.AppDatabase
import br.pucpr.appdev.lojaonline.database.CarrinhoDao
import br.pucpr.appdev.lojaonline.database.ProdutoDao
import br.pucpr.appdev.lojaonline.database.UserDao
import br.pucpr.appdev.lojaonline.repository.CarrinhoRepository
import br.pucpr.appdev.lojaonline.repository.ProdutoRepository
import br.pucpr.appdev.lojaonline.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "loja_online_database").build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideProdutoDao(appDatabase: AppDatabase): ProdutoDao {
        return appDatabase.produtoDao()
    }

    @Provides
    fun provideCarrinhoDao(appDatabase: AppDatabase): CarrinhoDao {
        return appDatabase.carrinhoDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    fun provideProdutoRepository(produtoDao: ProdutoDao): ProdutoRepository {
        return ProdutoRepository(produtoDao)
    }

    @Provides
    fun provideCarrinhoRepository(carrinhoDao: CarrinhoDao): CarrinhoRepository{
        return CarrinhoRepository(carrinhoDao)
    }
}