package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val type: String, // "Persona", "Avatar", "Enhanced"
    val createdTime: String,
    val status: String, // "Completed", "In Progress"
    val prompt: String,
    val imageResName: String // "img_minimal_pro", "img_street_style", "img_biz_headshot", "img_creator_port", "img_fashion_port"
)
