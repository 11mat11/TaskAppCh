package com.example.taskappch

import java.text.SimpleDateFormat
import java.util.Locale
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskappch.database.Task
import com.example.taskappch.databinding.TaskItemBinding
import java.util.Date

class TaskListAdapter(
    private val onEditClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit,
    private val onSwitchClick: (Task) -> Unit
) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, onEditClick, onDeleteClick, onSwitchClick)
    }

    class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: Task,
            onEditClick: (Task) -> Unit,
            onDeleteClick: (Task) -> Unit,
            onSwitchClick: (Task) -> Unit
        ) {
            binding.taskTitle.text = task.title

            // Ustawienie formatu daty i godziny
            val dateFormat = SimpleDateFormat("HH:mm d.MM.yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(task.timestamp))
            binding.taskDueDate.text = formattedDate

            // Ustawienie stanu przełącznika zgodnie z bazą danych
            binding.taskSwitch.isChecked = task.enabled

            // Obsługa kliknięcia na edycję i usuwanie
            binding.root.setOnClickListener { onEditClick(task) }
            binding.deleteButton.setOnClickListener { onDeleteClick(task) }

            // Obsługa przełącznika
            binding.taskSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (task.enabled != isChecked) {
                    onSwitchClick(task.copy(enabled = isChecked))    // Zapis w bazie danych
                }
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}

