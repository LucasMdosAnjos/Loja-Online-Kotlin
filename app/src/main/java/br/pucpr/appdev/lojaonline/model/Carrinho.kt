package br.pucpr.appdev.lojaonline.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Produto::class,
    parentColumns = ["id"],
    childColumns = ["produtoId"],
    onDelete = ForeignKey.CASCADE
)])
data class Carrinho(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val produtoId: Int
)