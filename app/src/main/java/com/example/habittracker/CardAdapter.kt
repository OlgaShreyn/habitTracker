package com.example.habittracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class CardsAdapter(
    private val onItemClickListener: View.OnClickListener,
    private val cards: MutableList<Habit>
) :
    RecyclerView.Adapter<CardsAdapter.CardViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        view.setOnClickListener(onItemClickListener)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    fun addCard(habit: Habit) {
        cards.add(habit)
        this.notifyItemInserted(itemCount)
    }

    fun changeCard(habit: Habit, index:Int) {
        cards[index] = habit
        this.notifyItemChanged(index)
    }

    override fun getItemCount(): Int = cards.size

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById<View>(R.id.habit_name) as TextView
        private val description: TextView = itemView.findViewById<View>(R.id.habit_description) as TextView
        private val priority: TextView = itemView.findViewById<View>(R.id.habit_priority) as TextView
        private val periodicity: TextView = itemView.findViewById<View>(R.id.periodicity) as TextView
        private val type: TextView = itemView.findViewById<View>(R.id.habit_type) as TextView
        private val card: CardView = itemView.findViewById<View>(R.id.habit) as CardView

        @SuppressLint("SetTextI18n")
        fun bind(habit: Habit) {
            title.text = habit.name
            description.text = habit.description
            priority.text = when (habit.priority.toString()) {
                "Low" -> "???????????? ??????????????????"
                "Middle" -> "?????????????? ??????????????????"
                "High" -> "?????????????? ??????????????????"
                else -> "?????????????????????? ??????????????????"
            }
            periodicity.text = "?????????????????? ${habit.count} ?????? ?? ${habit.days} ????????"
            type.text = when (habit.type.toString()) {
                "Good" -> "?????????????? ????????????????"
                "Bad" -> "???????????? ????????????????"
                else -> "?????????????????????? ??????"
            }
            try {
                card.setCardBackgroundColor(habit.color)
            } catch (_: IllegalArgumentException) {
            }
        }
    }
}