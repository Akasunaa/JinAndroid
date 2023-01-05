import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexprom.app.R
import com.alexprom.app.tasklist.Task
import java.util.*

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldTask: Task, newTask: Task): Boolean {
        return (oldTask.id == newTask.id)
    }

    override fun areContentsTheSame(oldTask: Task, newTask: Task): Boolean {
        return (oldTask == newTask)
    }
}

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit : (Task) -> Unit = {}

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val textView = itemView.findViewById<TextView>(R.id.task_title)
            textView.text = task.title
            val textViewdesc = itemView.findViewById<TextView>(R.id.task_description)
            textViewdesc.text = task.description
            val deleteButton = itemView.findViewById<ImageButton>(R.id.delete_button)
            deleteButton.setOnClickListener { onClickDelete(task) }
            val editButton = itemView.findViewById<ImageButton>(R.id.edit_button)
            editButton.setOnClickListener { onClickEdit(task) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position]);
    }
}