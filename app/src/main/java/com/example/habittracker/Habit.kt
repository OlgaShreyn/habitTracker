package com.example.habittracker

class Habit(
    val name: String,
    val description: String,
    val type: HabitType,
    val period: String,
    val priority: Int,
    val color: String?
)