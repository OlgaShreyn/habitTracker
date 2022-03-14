package com.example.habittracker

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val name: String,
    val description: String,
    val type: HabitType,
    val count: Int,
    val days: Int,
    val priority: HabitPriority,
    val color: Int
)