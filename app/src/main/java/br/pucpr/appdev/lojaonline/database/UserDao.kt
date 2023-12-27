package br.pucpr.appdev.lojaonline.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.pucpr.appdev.lojaonline.model.User

@Dao
interface UserDao {
    // Método para inserir um usuário
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>


    @Query("SELECT COUNT(*) FROM User WHERE email = :email")
    suspend fun countUsersWithEmail(email: String): Int

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    suspend fun getUserById(id:Int): User
}