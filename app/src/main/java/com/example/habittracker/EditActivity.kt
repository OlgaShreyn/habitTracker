package com.example.habittracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.example.habittracker.databinding.ActivityEditBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class EditActivity : AppCompatActivity() {
    companion object {
        const val EDIT_HABIT = "habit"

        fun getIntent(context: Context, habit: Habit? = null, position: Int = -1): Intent {
            return Intent(context, EditActivity::class.java).also {
                if (habit != null) {
                    val bundle = Bundle().apply {
                        putString("CARD_JSON", Json.encodeToString(habit))
                        putInt("CARD_POSITION", position) // todo change
                    }
                    it.putExtra(EDIT_HABIT, bundle)
                }
            }
        }
    }

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var position = -1
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EDIT_HABIT)) {
            title = getText(R.string.edit_habit)
            val habitString = intent.getBundleExtra("habit")?.getString("CARD_JSON")
            val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
            position = intent.getBundleExtra("habit")?.getInt("CARD_POSITION") ?: -1

            binding.apply {
                editHabitName.setText(habit.name)
                editHabitDescription.setText(habit.description)
                editHabitTimes.setText(habit.count.toString())
                editHabitDays.setText(habit.days.toString())
                (habitTypeGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true
                editHabitPriority.setSelection(habit.priority.ordinal)
            }
        }
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
                putString("CARD_JSON", Json { ignoreUnknownKeys = true }.encodeToString(habit))
                putInt("CARD_POSITION", position) // todo change
            }
            intent.putExtra(EDIT_HABIT, bundle)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}