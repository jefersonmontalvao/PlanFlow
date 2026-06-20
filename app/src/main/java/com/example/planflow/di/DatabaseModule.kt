package com.example.planflow.di

import android.content.Context
import androidx.room.Room
import com.example.planflow.data.local.database.AppDatabase
import com.example.planflow.data.local.database.daos.TransactionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "planflow_db")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideTransactionsDao(database: AppDatabase): TransactionsDao {
        return database.transactionsDao()
    }
}

