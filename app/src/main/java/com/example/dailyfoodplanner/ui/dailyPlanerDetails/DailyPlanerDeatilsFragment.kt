package com.example.dailyfoodplanner.ui.dailyPlanerDetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.dailyfoodplanner.R
import dagger.android.support.DaggerFragment

/**
 * A simple [Fragment] subclass.
 */
class DailyPlanerDeatilsFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_planer_deatils, container, false)
    }


}
