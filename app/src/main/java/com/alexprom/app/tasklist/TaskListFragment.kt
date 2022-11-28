package com.alexprom.app.tasklist

import TaskListAdapter
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alexprom.app.R
import com.alexprom.app.databinding.FragmentTaskListBinding
import com.alexprom.app.detail.DetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3"),
    )
    private val adapter = TaskListAdapter()
    private var binding: FragmentTaskListBinding? = null
    //private val intent = Intent(context, DetailActivity::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter.submitList(taskList)
        adapter.onClickDelete = {task -> taskList = taskList - task; RefreshAdapter()}
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        val rootView = binding?.root
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recycleView?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener{
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            RefreshAdapter()
        }
    }

    fun RefreshAdapter(){
        adapter.submitList(taskList)
        adapter.notifyDataSetChanged()
    }

}