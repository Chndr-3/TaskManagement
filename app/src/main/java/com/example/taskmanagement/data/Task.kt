package com.example.taskmanagement.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val name: String,
    val priority : Priority,
    var visibility: Boolean = false,
    val date: String

)



enum class Priority {
    LOW, MEDIUM, HIGH
}