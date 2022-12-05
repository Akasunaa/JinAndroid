package com.alexprom.app.tasklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id")
    val id: String = "id_0",
    @SerialName("content")
    var title: String = "Task 0",
    @SerialName("description")
    var description: String = "desc 0"): java.io.Serializable{
}
