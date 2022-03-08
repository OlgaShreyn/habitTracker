package com.example.habittracker

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ActivityMainBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val cards = mutableListOf(
    Habit("a", "a", HabitType.Good, 2, 1, HabitPriority.Middle, "blue"),
    Habit("aa", "aaa", HabitType.Good, 2, 2, HabitPriority.High, "green"),
    Habit("aaa", "aaa", HabitType.Bad, 2, 2, HabitPriority.Low, "gray"),
)


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CardsAdapter

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
        adapter = CardsAdapter(cards)
        cardsRecyclerView.adapter = adapter

        val resultAddLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val habitString = intent.getBundleExtra("habit")?.getString("CARD_JSON")
                    val habit = Json{ ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
                    adapter.addCard(habit)
                }
            }
        }
        binding.addButton.setOnClickListener {
            resultAddLauncher.launch(EditActivity.getIntent(this))
        }
    }
}