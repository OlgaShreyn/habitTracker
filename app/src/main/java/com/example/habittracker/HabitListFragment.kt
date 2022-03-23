package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.MainActivity.Companion.habits
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class HabitListFragment : Fragment() {

    private var adapter: CardsAdapter? = null
    private var habitType = ""
    var callback: HabitChangeRequestListenner? = null

    companion object {
        const val  GOOD_HABITS = "GOOD_HABITS"
        const val BAD_HABITS = "BAD_HABITS"
        private const val HABIT_TYPE = "HABIT_TYPE"

        fun newInstance(habitType: String): HabitListFragment {
            return HabitListFragment().apply { arguments = bundleOf(HABIT_TYPE to habitType) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitChangeRequestListenner
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    private fun getCardClickListener(cardsRecycler: RecyclerView): View.OnClickListener {
        return View.OnClickListener { view ->
            if (view == null) return@OnClickListener
            val itemPosition: Int = cardsRecycler.getChildLayoutPosition(view)
            val item: Habit = habits[itemPosition]
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            habitType = it.getString(HABIT_TYPE, GOOD_HABITS)
        }
        val habitsList = view.findViewById<RecyclerView>(R.id.cards_view)
        habitsList.layoutManager = LinearLayoutManager(activity)
        if (habits.size > 0)

       adapter =when (habitType) {
            GOOD_HABITS -> CardsAdapter(getCardClickListener(habitsList),
               habits.filter{ habit -> habit.type == HabitType.Good } as MutableList<Habit>)
            //{ itemClicked, pos ->
//                callback?.onHabitChangeRequest(itemClicked, pos, MainActivity.HABIT_EDIT_REQUEST)
//            }
            else -> CardsAdapter(getCardClickListener(habitsList),
                habits.filter{ habit -> habit.type == HabitType.Bad } as MutableList<Habit>)
            //{ itemClicked, pos ->
//                callback?.onHabitChangeRequest(itemClicked, pos, MainActivity.HABIT_EDIT_REQUEST)
//            }
       }

        parentFragmentManager.setFragmentResultListener(MainActivity.HABITS_ADD_REQUEST,
            viewLifecycleOwner) { _, data ->

            val bundle = data.getBundle(EDIT_HABIT)
            val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(bundle?.getString(CARD_JSON) ?: "")


            habit?.let {
                adapter?.addCard(it)
                habitsList.adapter = adapter
            }
        }
    }

    interface HabitChangeRequestListenner {
        fun onHabitChangeRequest(habit: Habit?, pos: Int?, status: String)
    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }
}