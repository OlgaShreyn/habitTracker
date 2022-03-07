package com.example.habittracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class CardsAdapter(
    private val cards: MutableList<Habit>
) :
    RecyclerView.Adapter<CardsAdapter.CardViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardViewHolder(inflater.inflate(R.layout.card_item, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById<View>(R.id.habit_name) as TextView
        private val description: TextView = itemView.findViewById<View>(R.id.habit_description) as TextView
        private val priority: TextView = itemView.findViewById<View>(R.id.habit_priority) as TextView
        private val periodicity: TextView = itemView.findViewById<View>(R.id.habit_period) as TextView
        private val type: TextView = itemView.findViewById<View>(R.id.habit_type) as TextView
        private val card: CardView = itemView.findViewById<View>(R.id.habit) as CardView

        fun bind(habit: Habit) {
            title.text = habit.name
            description.text = habit.description
            priority.text = habit.priority.toString()
            periodicity.text = habit.period
            type.text = habit.type.toString()
            try {
                card.setCardBackgroundColor(Color.parseColor(habit.color))
            } catch (_: IllegalArgumentException) {
            }
        }
    }
}