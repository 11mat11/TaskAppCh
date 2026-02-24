// TaskViewModel.kt
package com.example.taskappch.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.taskappch.TaskDatabase
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    // Direct access to TaskDao
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()

    // LiveData containing all tasks
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    // Methods to sort tasks in different ways
    fun sortByCreationDate(): LiveData<List<Task>> = taskDao.sortByCreationDate()
    fun sortByDueDate(): LiveData<List<Task>> = taskDao.sortByDueDate()
    fun sortByAlphabet(): LiveData<List<Task>> = taskDao.sortByAlphabet()

    // Retrieve a task by its ID
    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }

    // Functions to perform database operations asynchronously
    fun delete(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }

    fun insert(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }
}