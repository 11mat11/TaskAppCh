package com.example.taskappch

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskappch.database.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Query("SELECT * FROM task_table ORDER BY timestamp DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun sortByCreationDate(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY timestamp ASC")
    fun sortByDueDate(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY title ASC")
    fun sortByAlphabet(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>
    @Update
    suspend fun update(task: Task)
    @Delete
    suspend fun delete(task: Task)
}


