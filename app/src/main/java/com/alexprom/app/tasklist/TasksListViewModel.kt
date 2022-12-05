package com.alexprom.app.tasklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexprom.app.data.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TasksListViewModel : ViewModel() {
    private val webService = Api.taskWebService
    public val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())


    fun refresh() {
        viewModelScope.launch {
            val response = webService.fetchTasks() // Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedTasks = response.body()!!
            tasksStateFlow.value = fetchedTasks // on modifie le flow, ce qui déclenche ses observers
        }
    }
    
    fun add(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }
            val newTask = response.body()!!
            tasksStateFlow.value = tasksStateFlow.value + newTask
        }
    }
    fun remove(task: Task) {
        viewModelScope.launch {
            val response = webService.delete(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }
            tasksStateFlow.value = tasksStateFlow.value - task
        }
    }
    fun edit(task: Task) {
        viewModelScope.launch {
            val response = webService.update(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val updatedTask = response.body()!!
            tasksStateFlow.value = tasksStateFlow.value.map { if (it.id == updatedTask.id) updatedTask else it }
        }
    }


}