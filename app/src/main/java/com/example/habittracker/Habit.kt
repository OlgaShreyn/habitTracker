package com.example.habittracker

import android.os.Parcelable
import org.json.JSONObject
import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val name: String,
    val description: String,
    val type: HabitType,
    val executionCount: Int,
    val period: Int,
    val priority: Int,
    val color: String?
)