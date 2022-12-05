package com.alexprom.app.tasklist

import TaskListAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alexprom.app.data.Api
import com.alexprom.app.databinding.FragmentTaskListBinding
import com.alexprom.app.detail.DetailActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3"),
    )
    private val adapter = TaskListAdapter()
    private var binding: FragmentTaskListBinding? = null
    private val viewModel: TasksListViewModel by viewModels()


    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        taskList = taskList + task;
        adapter.submitList(taskList)
    }
    val editTask =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        taskList = taskList.map { if (it.id == task.id) task else it }
        adapter.submitList(taskList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter.submitList(taskList)
        adapter.onClickDelete = { task ->
            taskList = taskList - task
            adapter.submitList(taskList)
        }
        adapter.onClickEdit = { task ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val intent = Intent(context, DetailActivity::class.java)
        binding?.recycleView?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener{
            createTask.launch(intent)
        }
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            binding?.usernameTextView?.text = user.name
        }

    }

}