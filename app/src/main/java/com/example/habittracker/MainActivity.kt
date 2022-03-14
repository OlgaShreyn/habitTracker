package com.example.habittracker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ActivityMainBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val cards = mutableListOf<Habit>(
)

const val EDIT_HABIT = "EDIT HABIT"
const val CARD_POSITION = "CARD POSITION"
const val CARD_JSON = "CARD JSON"


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CardsAdapter
    private lateinit var resultEditLauncher: ActivityResultLauncher<Intent>

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
        adapter = CardsAdapter(getCardClickListener(cardsRecyclerView), cards)
        cardsRecyclerView.adapter = adapter

        val resultAddLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val habitString = intent.getBundleExtra(EDIT_HABIT)?.getString(CARD_JSON)
                    val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
                    adapter.addCard(habit)
                }
            }
        }

        resultEditLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val habitString = intent.getBundleExtra(EDIT_HABIT)?.getString(CARD_JSON)
                    val habitIndex = intent.getBundleExtra(EDIT_HABIT)?.getInt(CARD_POSITION)
                    val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
                    adapter.changeCard(habit, habitIndex ?: 0)
                }
            }
        }

        binding.addButton.setOnClickListener {
            resultAddLauncher.launch(EditActivity.getIntent(this))
        }
    }

    private fun getCardClickListener(cardsRecycler: RecyclerView): View.OnClickListener {
        return View.OnClickListener { view ->
            if (view == null) return@OnClickListener
            val itemPosition: Int = cardsRecycler.getChildLayoutPosition(view)
            val item: Habit = cards[itemPosition]

            resultEditLauncher.launch(EditActivity.getIntent(this, item, itemPosition))
        }
    }
}
