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
    Habit("a", "a", HabitType.Good, 2, 1, 1, "blue"),
    Habit("aa", "aaa", HabitType.Good, 2, 2, 2, "green"),
    Habit("aaa", "aaa", HabitType.Bad, 2, 2, 2, "gray"),
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
                println("check2")
                result.data?.let { intent ->
                    val strJson = intent.getBundleExtra("habit")
                    val a = strJson?.getString("CARD_JSON")
                    println("check3")
                    val ss = Json{ ignoreUnknownKeys = true }.decodeFromString<Habit>(a ?: "")
                    adapter.addCard(ss)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        binding.addButton.setOnClickListener {
            resultAddLauncher.launch(EditActivity.getIntent(this))
        }
    }
}