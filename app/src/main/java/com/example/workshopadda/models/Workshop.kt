package com.example.workshopadda.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workshopTable")
data class Workshop (
    @PrimaryKey
    val id: Int = -1,
    val name: String = "",
    val description: String = "",
    val fromDate: String = "",
    val toDate: String = ""
)