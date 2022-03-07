package com.example.habittracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ActivityMainBinding

val cards = mutableListOf(
    Habit("a", "a", HabitType.Good, "fuf", 1, "blue"),
    Habit("aa", "aaa", HabitType.Good, "fuf", 2, "green"),
    Habit("aaa", "aaa", HabitType.Bad, "fufufuf", 2, "gray"),
)


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cardsRecyclerView: RecyclerView = binding.cardsView
        cardsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        cardsRecyclerView.adapter = CardsAdapter(cards)
    }
}