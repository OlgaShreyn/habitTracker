package com.example.habittracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.databinding.ActivityEditBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class EditActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_HABIT = "habit"

        fun getIntent(context: Context, habit: Habit? = null): Intent {
            return Intent(context, EditActivity::class.java).also {
                if (habit != null) {
                    val bundle = Bundle().apply {
                        putString("CARD_JSON", Json.encodeToString(habit))
                        putInt("CARD_POSITION", 1) // todo change
                    }
                    it.putExtra(EXTRA_HABIT, bundle)
                }
            }
        }
    }
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.habitSaveButton.setOnClickListener {

            var type = HabitType.Bad
            if (binding.habitTypeGood.id == binding.habitTypeGroup.checkedRadioButtonId) {
                type = HabitType.Good
            }
            val priority = when (binding.editHabitPriority.selectedItem.toString()) {
                "Низкий" -> HabitPriority.Low
                "Средний" -> HabitPriority.Middle
                "Высокий" -> HabitPriority.High
                else -> HabitPriority.Low
            }
            val habit = Habit(
                binding.editHabitName.text.toString(),
                binding.editHabitDescription.text.toString(),
                type,
                binding.editHabitTimes.text.toString().toInt(),
                binding.editHabitDays.text.toString().toInt(),
                priority,
                "red"
                )
            val intent = Intent()
            val bundle = Bundle().apply {
                putString("CARD_JSON", Json{ ignoreUnknownKeys = true }.encodeToString(habit))
                putInt("CARD_POSITION", 1) // todo change
            }
            intent.putExtra(EXTRA_HABIT, bundle)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}