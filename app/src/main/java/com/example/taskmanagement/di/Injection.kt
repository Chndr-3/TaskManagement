package com.example.taskmanagement.di

import android.content.Context
import androidx.room.Room
import com.example.taskmanagement.data.AppDatabase
import com.example.taskmanagement.data.TaskDao
import com.example.taskmanagement.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "room_database"
        ).build()
    }
    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase) : TaskDao{
        return appDatabase.taskDao()
    }
    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }
}