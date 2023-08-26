package com.example.taskmanagement.di

import android.content.Context
import com.example.taskmanagement.data.AppDatabase
import com.example.taskmanagement.data.TaskRepository

object Injection {

    fun provideRepository(context: Context): TaskRepository {
        val database = AppDatabase.getDatabase(context)
        val dao = database.taskDao()
        return TaskRepository.getInstance(dao)
    }
}