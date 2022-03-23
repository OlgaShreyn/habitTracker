package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


const val EDIT_HABIT = "EDIT HABIT"
const val CARD_POSITION = "CARD POSITION"
const val CARD_JSON = "CARD JSON"


class MainActivity : AppCompatActivity(), EditFragment.OnHabitSelectedListener,
    HabitListFragment.HabitChangeRequestListenner, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var adapter: CardsAdapter
    private lateinit var resultEditLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val HABITS_ADD_REQUEST = "HABITS_ADD_REQUEST"
        const val HABIT_EDIT_REQUEST = "HABIT_EDIT_REQUEST"
        val habits = mutableListOf<Habit>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//todo: через newInstance
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, homeFragment, "homeFragment")
            .commit()
        navigation_view.setNavigationItemSelectedListener(this)


//        val cardsRecyclerView: RecyclerView = binding.cardsView
//        cardsRecyclerView.layoutManager = LinearLayoutManager(
//            this,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        adapter = CardsAdapter(getCardClickListener(cardsRecyclerView), cards)
//        cardsRecyclerView.adapter = adapter
//
//        val resultAddLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data?.let { intent ->
//                    val habitString = intent.getBundleExtra(EDIT_HABIT)?.getString(CARD_JSON)
//                    val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
//                    adapter.addCard(habit)
//                }
//            }
//        }
//
//        resultEditLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data?.let { intent ->
//                    val habitString = intent.getBundleExtra(EDIT_HABIT)?.getString(CARD_JSON)
//                    val habitIndex = intent.getBundleExtra(EDIT_HABIT)?.getInt(CARD_POSITION)
//                    val habit = Json { ignoreUnknownKeys = true }.decodeFromString<Habit>(habitString ?: "")
//                    adapter.changeCard(habit, habitIndex ?: 0)
//                }
//            }
//        }
//
//        binding.addButton.setOnClickListener {
//            resultAddLauncher.launch(EditActivity.getIntent(this))
//        }
    }

//    private fun getCardClickListener(cardsRecycler: RecyclerView): View.OnClickListener {
//        return View.OnClickListener { view ->
//            if (view == null) return@OnClickListener
//            val itemPosition: Int = cardsRecycler.getChildLayoutPosition(view)
//            val item: Habit = habits[itemPosition]
//
//            resultEditLauncher.launch(EditActivity.getIntent(this, item, itemPosition))
//        }
//    }

     override fun onHabitChange(habit: Habit?, pos: Int?, status_code: String?) {
         println("aaaaaaaaaaaa")
        if (status_code == "HABIT_EDIT_REQUEST" && pos != null && habit != null) {
            habits[pos] = habit
        } else if (status_code == "HABITS_ADD_REQUEST") {
            if (habit != null) {
                habits.add(habit)
            }

        }
        if (habits.size > 0)
            supportFragmentManager.findFragmentByTag("newHabitFragment")?.let {
                supportFragmentManager.beginTransaction()
                    .remove(it)
                    .add(R.id.fragment_container, HomeFragment(), "homeFragment")
                    .commit()
            }
    }

    override fun onHabitChangeRequest(habit: Habit?, pos: Int?, status: String) {
        var newPos: Int? = null
        if (habit != null)
            newPos = habits.indexOf(habit)
        val fragment = EditFragment.newInstance(habit, newPos, status)
        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.findFragmentByTag("homeFragment")!!)
            .add(R.id.fragment_container, fragment, "newHabitFragment")
            .commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_home -> {
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment !is NavHostFragment) {
                        supportFragmentManager.beginTransaction().remove(fragment).commit()

                    }
                }

                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, HomeFragment(), "homeFragment")
                    .commit()
            }
            R.id.menu_item_about -> {
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment !is NavHostFragment)
                        supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, AboutFragment(), "aboutFragment")
                    .commit()

            }
        }
        adapter.notifyDataSetChanged()
        //navigation_drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
