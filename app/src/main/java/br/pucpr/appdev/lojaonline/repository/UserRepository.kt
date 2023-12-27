package br.pucpr.appdev.lojaonline.repository

import br.pucpr.appdev.lojaonline.database.UserDao
import br.pucpr.appdev.lojaonline.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun createUser(email: String, password: String): Boolean {
        if (userDao.countUsersWithEmail(email) > 0) {
            // Usuário com este e-mail já existe
            return false
        }

        val newUser = User(email = email, password = password)
        val rowId = userDao.insertUser(newUser)
        return rowId != -1L  // Retorna true se o usuário foi inserido com sucesso
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun getUserById(id:Int): User{
        return userDao.getUserById(id)
    }
}