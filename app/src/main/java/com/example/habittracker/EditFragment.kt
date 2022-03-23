package com.example.habittracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.habittracker.databinding.FragmentEditBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class EditFragment : Fragment() {
    companion object {

        fun newInstance(habit: Habit?, position: Int? = -1, status_code: String?): EditFragment {
            val fragment = EditFragment()
            if (habit != null) {
                val bundle = Bundle().apply {
                    putString(CARD_JSON, Json.encodeToString(habit))
                    if (position != null) {
                        putInt(CARD_POSITION, position)
                    }
                    putString("Status_code", status_code)
                }
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    var habit: Habit? = null
    var status_code: String? = null
    private var position: Int = -1
    private var selectedColor = Color.WHITE

    private var _binding: FragmentEditBinding? = null
    private val binding
        get() = _binding!!

    private var callback: OnHabitSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isColorPickerBuild = false
        arguments.let {
            if (it != null) {
                habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(it.getString(CARD_JSON) ?: "")
                position = it.getInt(CARD_POSITION)
                status_code = it.getString("Status_code")
            }
        }

        if (habit != null) {

            binding.apply {
                editHabitName.setText(habit!!.name)
                editHabitDescription.setText(habit!!.description)
                editHabitTimes.setText(habit!!.count.toString())
                editHabitDays.setText(habit!!.days.toString())
                (habitTypeGroup.getChildAt(habit!!.type.ordinal) as RadioButton).isChecked = true
                editHabitPriority.setSelection(habit!!.priority.ordinal)
                selectedColorSquare.canvasBackgroundColor = habit!!.color
            }
        }

        binding.habitSaveButton.setOnClickListener {
            if (binding.editHabitName.text.toString().trim().isEmpty()) {
                binding.editHabitName.error = getString(R.string.required_field_err)
                return@setOnClickListener
            } else {
                binding.habitName.error = null
            }
            if (binding.editHabitTimes.text.toString().trim().isEmpty() ||
                binding.editHabitDays.text.toString().trim().isEmpty()
            ) {
                binding.editHabitDays.error = getString(R.string.required_field_err)
                return@setOnClickListener
            }
            val type = when (binding.habitTypeGroup.checkedRadioButtonId) {
                binding.habitTypeGood.id -> HabitType.Good
                binding.habitTypeBad.id -> HabitType.Bad
                else -> HabitType.Good
            }
            val priority = when (binding.editHabitPriority.selectedItem.toString()) {
                "Низкий" -> HabitPriority.Low
                "Средний" -> HabitPriority.Middle
                "Высокий" -> HabitPriority.High
                else -> HabitPriority.Low
            }
            val newHabit = Habit(
                binding.editHabitName.text.toString(),
                binding.editHabitDescription.text.toString(),
                type,
                binding.editHabitTimes.text.toString().toInt(),
                binding.editHabitDays.text.toString().toInt(),
                priority,
                selectedColor
            )

            println(newHabit)

            val key = if (habit == null) MainActivity.HABITS_ADD_REQUEST else MainActivity.HABIT_EDIT_REQUEST
            val bundle = Bundle().apply {
               putString(CARD_JSON, Json { ignoreUnknownKeys = true }.encodeToString(newHabit))
                putInt(CARD_POSITION, position) //аннотации,
            }
            parentFragmentManager.setFragmentResult(EDIT_HABIT, bundle)
            //findNavController().popBackStack()

//            val bundle = Bundle().apply {
//                putString(CARD_JSON, Json { ignoreUnknownKeys = true }.encodeToString(newHabit))
//                putInt(CARD_POSITION, position) //аннотации,
//            }
//            parentFragmentManager.setFragmentResult(CARD_JSON, bundle)
//            callback?.onHabitChange(habit, position, status_code)
        }

        binding.habitUndoButton.setOnClickListener {

        }

        binding.selectedColorSquare.setOnClickListener {
            if (binding.textBeforeColorPicker.isVisible) {
                binding.textBeforeColorPicker.visibility = View.INVISIBLE
                binding.colorsPickerView.visibility = View.INVISIBLE
            } else {
                if (!isColorPickerBuild) {
                    isColorPickerBuild = buildColorPicker()
                }
                binding.colorsPickerView.visibility = View.VISIBLE
                binding.textBeforeColorPicker.visibility = View.VISIBLE
            }
        }

        binding.selectedColorSquare.setOnClickListener {
            if (binding.textBeforeColorPicker.isVisible) {
                binding.textBeforeColorPicker.visibility = View.INVISIBLE
                binding.colorsPickerView.visibility = View.INVISIBLE
            } else {
                if (!isColorPickerBuild) {
                    isColorPickerBuild = buildColorPicker()
                }
                binding.colorsPickerView.visibility = View.VISIBLE
                binding.textBeforeColorPicker.visibility = View.VISIBLE
            }
        }
    }

    private fun getScreenWidth(activity: Activity): Int {
        val windowMetrics = activity.windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        return windowMetrics.bounds.width() - insets.left - insets.right
    }

    @SuppressLint("SetTextI18n")
    private fun buildColorPicker(): Boolean {
        val squareCount = 16
        val layoutParams = setSizeForSquareAndMargins()
        val initColorDegree: Float = 360 / squareCount.toFloat()

        val colors = ArrayList<Int>(squareCount)
        for (i in 0 until squareCount) {
            val colorDegree = initColorDegree + (initColorDegree * i)
            colors.add(Color.HSVToColor(floatArrayOf(colorDegree, 100f, 100f)))
        }

        val rainbow = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.RED, *colors.toIntArray(), Color.RED)
        )
        rainbow.gradientType = GradientDrawable.LINEAR_GRADIENT
        rainbow.shape = GradientDrawable.RECTANGLE
        binding.colorsPickerView.background = rainbow

        for (i in 0 until squareCount) {
            val colorSquareView = ColorPickerView(requireActivity(), layoutParams, colors[i])

            colorSquareView.setOnClickListener {
                val clickedSquareView = it as ColorPickerView
                selectedColor = clickedSquareView.canvasBackgroundColor
                binding.selectedColorSquare.canvasBackgroundColor = selectedColor

                val hsv = FloatArray(3)
                Color.colorToHSV(selectedColor, hsv)
                binding.hsv.text = formatColorStrings("HSV", hsv[0].toInt(), hsv[1].toInt(), hsv[2].toInt())
                binding.rgb.text = formatColorStrings(
                    "RGB",
                    Color.red(selectedColor),
                    Color.green(selectedColor),
                    Color.blue(selectedColor)
                )
            }
            binding.colorsPickerView.addView(colorSquareView)
        }
        return true
    }

    private fun formatColorStrings(format: String, f: Int, s: Int, t: Int): String {
        return "$format: $f;$s;$t"
    }

    private fun setSizeForSquareAndMargins(): LinearLayout.LayoutParams {
        val squareOnScreenCount = 4
        val squareMarginCount = squareOnScreenCount + 1
        val squareMarginWidthPercent = 0.3
        val screenWidth =
            getScreenWidth(requireActivity()) - binding.rootLinearLayout.paddingRight - binding.rootLinearLayout.paddingLeft
        val squareWidth: Double = screenWidth / (squareMarginCount * squareMarginWidthPercent + squareOnScreenCount)
        val squareMarginWidth: Double = squareWidth * squareMarginWidthPercent
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(squareMarginWidth.toInt())
        layoutParams.width = squareWidth.toInt()
        layoutParams.height = squareWidth.toInt()
        return layoutParams
    }

    interface OnHabitSelectedListener {
        fun onHabitChange(habit: Habit?, pos: Int?, status_code: String?)
    }

}

//
//    private var selectedColor = Color.WHITE
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        var isColorPickerBuild = false
//        title = getString(R.string.add_habit)
//        var position = -1
//        binding = ActivityEditBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        if (intent.hasExtra(EDIT_HABIT)) {
//            title = getText(R.string.edit_habit)
//            val habitString = intent.getBundleExtra(EDIT_HABIT)?.getString(CARD_JSON)
//            val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
//            position = intent.getBundleExtra(EDIT_HABIT)?.getInt(CARD_POSITION) ?: -1
//
//            binding.apply {
//                editHabitName.setText(habit.name)
//                editHabitDescription.setText(habit.description)
//                editHabitTimes.setText(habit.count.toString())
//                editHabitDays.setText(habit.days.toString())
//                (habitTypeGroup.getChildAt(habit.type.ordinal) as RadioButton).isChecked = true
//                editHabitPriority.setSelection(habit.priority.ordinal)
//                selectedColorSquare.canvasBackgroundColor = habit.color
//            }
//        }
//        binding.habitSaveButton.setOnClickListener {
//            if (binding.editHabitName.text.toString().trim().isEmpty()) {
//                binding.editHabitName.error = getString(R.string.required_field_err)
//                return@setOnClickListener
//            } else {
//                binding.habitName.error = null
//            }
//            if (binding.editHabitTimes.text.toString().trim().isEmpty() ||
//                binding.editHabitDays.text.toString().trim().isEmpty()
//            ) {
//                binding.editHabitDays.error = getString(R.string.required_field_err)
//                return@setOnClickListener
//            }
//            val type = when (binding.habitTypeGroup.checkedRadioButtonId) {
//                binding.habitTypeGood.id -> HabitType.Good
//                binding.habitTypeBad.id -> HabitType.Bad
//                else -> HabitType.Good
//            }
//            val priority = when (binding.editHabitPriority.selectedItem.toString()) {
//                "Низкий" -> HabitPriority.Low
//                "Средний" -> HabitPriority.Middle
//                "Высокий" -> HabitPriority.High
//                else -> HabitPriority.Low
//            }
//            val habit = Habit(
//                binding.editHabitName.text.toString(),
//                binding.editHabitDescription.text.toString(),
//                type,
//                binding.editHabitTimes.text.toString().toInt(),
//                binding.editHabitDays.text.toString().toInt(),
//                priority,
//                selectedColor
//            )
//            val intent = Intent()
//            val bundle = Bundle().apply {
//                //putParcelable()
//                putString(CARD_JSON, Json { ignoreUnknownKeys = true }.encodeToString(habit))
//                putInt(CARD_POSITION, position) //аннотации,
//            }
//            intent.putExtra(EDIT_HABIT, bundle)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
//
//        binding.habitUndoButton.setOnClickListener {
//            finish()
//        }
//
//        binding.selectedColorSquare.setOnClickListener {
//            if (binding.textBeforeColorPicker.isVisible) {
//                binding.textBeforeColorPicker.visibility = View.INVISIBLE
//                binding.colorsPickerView.visibility = View.INVISIBLE
//            } else {
//                if (!isColorPickerBuild) {
//                    isColorPickerBuild = buildColorPicker()
//                }
//                binding.colorsPickerView.visibility = View.VISIBLE
//                binding.textBeforeColorPicker.visibility = View.VISIBLE
//            }
//        }
//    }
//
//    private fun getScreenWidth(activity: Activity): Int {
//        val windowMetrics = activity.windowManager.currentWindowMetrics
//        val insets: Insets = windowMetrics.windowInsets
//            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
//        return windowMetrics.bounds.width() - insets.left - insets.right
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun buildColorPicker():Boolean {
//        val squareCount = 16
//        val layoutParams = setSizeForSquareAndMargins()
//        val initColorDegree: Float = 360 / squareCount.toFloat()
//
//        val colors = ArrayList<Int>(squareCount)
//        for (i in 0 until squareCount) {
//            val colorDegree = initColorDegree + (initColorDegree * i)
//            colors.add(Color.HSVToColor(floatArrayOf(colorDegree, 100f, 100f)))
//        }
//
//        val rainbow = GradientDrawable(
//            GradientDrawable.Orientation.LEFT_RIGHT,
//            intArrayOf(Color.RED, *colors.toIntArray(), Color.RED)
//        )
//        rainbow.gradientType = GradientDrawable.LINEAR_GRADIENT
//        rainbow.shape = GradientDrawable.RECTANGLE
//        binding.colorsPickerView.background = rainbow
//
//        for (i in 0 until squareCount) {
//            val colorSquareView = ColorPickerView(this, layoutParams, colors[i])
//
//            colorSquareView.setOnClickListener {
//                val clickedSquareView = it as ColorPickerView
//                selectedColor = clickedSquareView.canvasBackgroundColor
//                binding.selectedColorSquare.canvasBackgroundColor = selectedColor
//
//                val hsv = FloatArray(3)
//                Color.colorToHSV(selectedColor, hsv)
//                binding.hsv.text = formatColorStrings("HSV", hsv[0].toInt(), hsv[1].toInt(), hsv[2].toInt())
//                binding.rgb.text = formatColorStrings(
//                    "RGB",
//                    Color.red(selectedColor),
//                    Color.green(selectedColor),
//                    Color.blue(selectedColor)
//                )
//            }
//            binding.colorsPickerView.addView(colorSquareView)
//        }
//        return true
//    }
//
//    private fun formatColorStrings(format: String, f: Int, s: Int, t: Int): String {
//        return "$format: $f;$s;$t"
//    }
//
//    private fun setSizeForSquareAndMargins(): LinearLayout.LayoutParams {
//        val squareOnScreenCount = 4
//        val squareMarginCount = squareOnScreenCount + 1
//        val squareMarginWidthPercent = 0.3
//        val screenWidth =
//            getScreenWidth(this) - binding.rootLinearLayout.paddingRight - binding.rootLinearLayout.paddingLeft
//        val squareWidth: Double = screenWidth / (squareMarginCount * squareMarginWidthPercent + squareOnScreenCount)
//        val squareMarginWidth: Double = squareWidth * squareMarginWidthPercent
//        val layoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        layoutParams.setMargins(squareMarginWidth.toInt())
//        layoutParams.width = squareWidth.toInt()
//        layoutParams.height = squareWidth.toInt()
//        return layoutParams
//    }
//}