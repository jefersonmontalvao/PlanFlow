package com.example.planflow.data.local.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.planflow.data.local.database.entities.TransactionEntity

@Dao
interface TransactionsDao {
    @Insert
    fun insert(transaction: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)

    @Update
    fun update(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id == :id")
    suspend fun getById(id: String): TransactionEntity?
}