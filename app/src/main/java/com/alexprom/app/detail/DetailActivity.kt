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
            AlexPromAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Detail(validateTask)
                }
            }
        }
    }
}

@Composable
fun Detail(onValidate: (Task) -> Unit ) {
    var newTask by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(), title = "New Task !"))}
    Column(Modifier.padding(16.dp),Arrangement.spacedBy(space = 16.dp)) {
        Text("Task Detail", style = MaterialTheme.typography.h1)
        OutlinedTextField(
            value = newTask.title,
            onValueChange = { newTask = newTask.copy(title = it) },
            label = { Text("Title")})
        OutlinedTextField(
            value = newTask.description,
            onValueChange = {newTask = newTask.copy(description = it) },
            label = { Text("Description")})
        Button(onClick = { onValidate(newTask);}) { Text("Valider") }
    }

}


@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    AlexPromAppTheme {
        //Detail()
    }
}