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
import coil.load
import com.alexprom.app.R
import com.alexprom.app.data.Api
import com.alexprom.app.databinding.FragmentTaskListBinding
import com.alexprom.app.detail.DetailActivity
import com.alexprom.app.user.UserActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private val adapter = TaskListAdapter()
    private var binding: FragmentTaskListBinding? = null
    private val viewModel: TasksListViewModel by viewModels()

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        viewModel.add(task)
    }
    val editTask =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task
        viewModel.edit(task)
    }

    val editAvatar  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ _ -> }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter.onClickDelete = { task ->
            viewModel.remove(task)
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
        val detailIntent = Intent(context, DetailActivity::class.java)
        val userIntent = Intent(context, UserActivity::class.java)
        binding?.recycleView?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener{
            createTask.launch(detailIntent)
        }
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.submitList(newList)
            }
        }

        binding?.imageView?.setOnClickListener{
            editAvatar.launch(userIntent)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        val imageView = binding?.imageView
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            binding?.usernameTextView?.text = user.name

            imageView?.load(user.avatar) {
                error(R.drawable.ic_launcher_background) // image par défaut en cas d'erreur
            }
        }
    }
}