package com.example.habittracker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator

//import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_home_pager.*



class HomeFragment : Fragment() {
    var callback: HabitListFragment.HabitChangeRequestListenner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pager, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as HabitListFragment.HabitChangeRequestListenner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager_home_fragment.adapter = HomePagerAdapter(this)

        TabLayoutMediator(tab_layout, view_pager_home_fragment) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.habit_type_bad)
                else -> getString(R.string.habit_type_good)
            }
        }.attach()

        val fab = view.findViewById<FloatingActionButton>(R.id.addButton)
        fab.setOnClickListener{
                setFragmentResultListener(MainActivity.HABITS_ADD_REQUEST) { key, bundle ->
                    // Здесь можно передать любой тип, поддерживаемый Bundle-ом
                    val result = bundle.getString(CARD_JSON)
                    println("get")
                    println(result)
                }
            callback?.onHabitChangeRequest(null, null, MainActivity.HABITS_ADD_REQUEST)
        }
        }
    }