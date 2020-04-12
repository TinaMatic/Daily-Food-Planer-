package com.example.dailyfoodplanner.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.DailyPlaner
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*
import javax.inject.Inject


class ScheduleFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var scheduleViewModel: ScheduleViewModel

    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleViewModel = ViewModelProvider(this, viewModelFactory).get(ScheduleViewModel::class.java)

        val monthList = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_months, monthList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = adapter

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        monthSpinner.setSelection(currentMonth)

        monthSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadAllDailyPlansForMonth(position + 1)
            }
        }
    }

    fun loadAllDailyPlansForMonth(month: Int){
        scheduleViewModel.readDailyPlansForMonth(month)

        scheduleViewModel.dailyPlansForMonthLiveData.observe(this, Observer {
            setScheduleAdapter(it)
            scheduleAdapter.notifyDataSetChanged()
        })
    }

    fun setScheduleAdapter(listOfDailyPlans: List<DailyPlaner>){
        scheduleAdapter = ScheduleAdapter(requireContext(), listOfDailyPlans)

        recyclerViewSchedule.layoutManager = LinearLayoutManager(context)
        recyclerViewSchedule.adapter = scheduleAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scheduleViewModel.clear()
    }
}