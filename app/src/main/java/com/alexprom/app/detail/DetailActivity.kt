package com.alexprom.app.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexprom.app.detail.ui.theme.AlexPromAppTheme
import com.alexprom.app.tasklist.Task
import java.util.*

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val validateTask = {
                    task : Task -> intent.putExtra("task",task)
                    setResult(RESULT_OK, intent)
                    finish()
            }
            val task = intent.getSerializableExtra("task") as Task?;
            AlexPromAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Detail(validateTask,task)
                }
            }
        }
    }

}

@Composable
fun Detail(onValidate: (Task) -> Unit, initialTask : Task? ) {
    val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")
    var task by remember { mutableStateOf(initialTask?:newTask)}
    Column(Modifier.padding(16.dp),Arrangement.spacedBy(space = 16.dp)) {
        Text("Task Detail", style = MaterialTheme.typography.h1)
        OutlinedTextField(
            value = task.title,
            onValueChange = { task = task.copy(title = it) },
            label = { Text("Title")})
        OutlinedTextField(
            value = task.description,
            onValueChange = {task = task.copy(description = it) },
            label = { Text("Description")})
        Button(onClick = { onValidate(task);}) { Text("Valider") }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    AlexPromAppTheme {
    }
}