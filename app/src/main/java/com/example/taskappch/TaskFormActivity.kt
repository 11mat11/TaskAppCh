// TaskFormActivity.kt
package com.example.taskappch

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskappch.database.Task
import com.example.taskappch.database.TaskViewModel
import java.util.*
import android.widget.TextView

import java.text.SimpleDateFormat

class TaskFormActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Int? = null
    private var dueDateTimestamp: Long = System.currentTimeMillis()
    private val dateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        val titleInput = findViewById<EditText>(R.id.titleInput)
        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        val dueDateText = findViewById<TextView>(R.id.dueDateText)
        val selectDateTimeButton = findViewById<LinearLayout>(R.id.selectDateTimeButton)
        val saveButton = findViewById<Button>(R.id.saveButton)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        taskId = intent.getIntExtra("TASK_ID", -1).takeIf { it != -1 }
        if (taskId != null) {
            taskViewModel.getTaskById(taskId!!).observe(this) { task ->
                task?.let {
                    titleInput.setText(it.title)
                    descriptionInput.setText(it.description)
                    dueDateTimestamp = it.timestamp
                    dueDateText.text = "Termin zadania: ${dateFormat.format(Date(dueDateTimestamp))}"
                    findViewById<TextView>(R.id.formTitle).text = "Edycja zadania"
                    saveButton.text = "UPDATE"
                }
            }
        } else {
            dueDateText.text = "Termin zadania: ${dateFormat.format(Date(dueDateTimestamp))}"
        }

        selectDateTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    TimePickerDialog(
                        this,
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            dueDateTimestamp = calendar.timeInMillis
                            dueDateText.text = "Termin: ${dateFormat.format(calendar.time)}"
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            val task = Task(id = taskId, title = title, description = description, timestamp = dueDateTimestamp, enabled = false)
            if (taskId == null) {
                taskViewModel.insert(task)
            } else {
                taskViewModel.update(task)
            }
            finish()
        }

        findViewById<TextView>(R.id.cancelButton).setOnClickListener {
            finish() // Zamknij aktywność bez zapisywania
        }
        findViewById<ImageView>(R.id.cancelButtons).setOnClickListener {
            finish() // Zamknij aktywność bez zapisywania
        }
    }
}
