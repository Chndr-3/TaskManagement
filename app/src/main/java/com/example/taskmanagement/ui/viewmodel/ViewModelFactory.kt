package com.example.taskmanagement.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.data.TaskRepository

class ViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return modelClass.getDeclaredConstructor(TaskRepository::class.java)
                .newInstance(repository)
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}