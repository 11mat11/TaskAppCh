package com.example.taskappch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskappch.database.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.google.android.material.button.MaterialButton
class MainActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // Inicjalizacja RecyclerView i Adaptera
        recyclerView = findViewById(R.id.recyclerView)
        val adapter = TaskListAdapter(
            onEditClick = { task ->
                val intent = Intent(this, TaskFormActivity::class.java)
                intent.putExtra("TASK_ID", task.id) // Przekazanie ID zadania do edycji
                startActivity(intent)
            },
            onDeleteClick = { task ->
                taskViewModel.delete(task) // Usuwanie zadania
            },
            onSwitchClick = { task ->
                taskViewModel.update(task) // Aktualizacja zadania w bazie danych
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicjalizacja ViewModel
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.allTasks.observe(this, { tasks ->
            tasks?.let { adapter.submitList(it.toMutableList()) }
        })
        taskViewModel.allTasks.observe(this, { tasks ->
            Log.d("TASK_LIST", "Tasks: $tasks")
            tasks?.let { adapter.submitList(it.toMutableList()) }
        })
        // Obsługa przycisku dodawania zadania
        findViewById<LinearLayout>(R.id.addButton).setOnClickListener {
            val intent = Intent(this, TaskFormActivity::class.java)
            startActivity(intent)
        }

        // Obsługa przycisku sortowania
        findViewById<LinearLayout>(R.id.menuButton).setOnClickListener {
            showSortMenu()
        }
    }

    // Funkcja wyświetlająca menu sortowania
    private fun showSortMenu() {
        val menuButton = findViewById<View>(R.id.menuButton)
        val popupMenu = PopupMenu(this, menuButton)
        popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_by_due_date -> {
                    taskViewModel.sortByDueDate().observe(this) { tasks ->
                        tasks?.let { (recyclerView.adapter as TaskListAdapter).submitList(it) }
                    }
                    true
                }
                R.id.sort_by_creation -> {
                    taskViewModel.sortByCreationDate().observe(this) { tasks ->
                        tasks?.let { (recyclerView.adapter as TaskListAdapter).submitList(it) }
                    }
                    true
                }
                R.id.sort_by_alphabet -> {
                    taskViewModel.sortByAlphabet().observe(this) { tasks ->
                        tasks?.let { (recyclerView.adapter as TaskListAdapter).submitList(it) }
                    }
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}