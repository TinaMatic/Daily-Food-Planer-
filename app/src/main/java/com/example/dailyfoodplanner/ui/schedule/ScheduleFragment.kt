package com.example.dailyfoodplanner.ui.schedule

import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.notification.AlarmScheduler
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ScheduleFragment : DaggerFragment(), ScheduleAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var scheduleViewModel: ScheduleViewModel

    private lateinit var scheduleAdapter: ScheduleAdapter

    private var compositeDisposable = CompositeDisposable()

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

    override fun onDestroyView() {
        super.onDestroyView()
        scheduleViewModel.clear()
        compositeDisposable.clear()
    }

    fun loadAllDailyPlansForMonth(month: Int){
        scheduleViewModel.readDailyPlansForMonth(month)

        scheduleViewModel.dailyPlansForMonthLiveData.observe(this, Observer {
            setScheduleAdapter(it)
            scheduleAdapter.notifyDataSetChanged()
        })

        scheduleViewModel.dailyPlansLoading.observe(this, Observer {
            showProgressBarSchedule(it)
        })
    }

    fun setScheduleAdapter(listOfDailyPlans: List<DailyPlaner>){
        scheduleAdapter = ScheduleAdapter(requireContext(), ArrayList(listOfDailyPlans))
        scheduleAdapter.setOnItemClickListener(this)

        recyclerViewSchedule.layoutManager = LinearLayoutManager(context)
        recyclerViewSchedule.adapter = scheduleAdapter
    }

    private fun showProgressBarSchedule(show: Boolean){
        if(show){
            progressBarSchedule.visibility = View.VISIBLE
            recyclerViewSchedule.visibility = View.INVISIBLE
        } else{
            progressBarSchedule.visibility = View.INVISIBLE
            recyclerViewSchedule.visibility = View.VISIBLE
        }
    }

    override fun deleteDailyPlan(dailyPlanId: String, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message))
            .setCancelable(true)
            .setPositiveButton("Yes"){_, _ ->
                scheduleViewModel.deleteDilyPlan(dailyPlanId).subscribe({
                    if (it) {
                        Toast.makeText(context, "Daily Plan successfully deleted", Toast.LENGTH_SHORT).show()
                        scheduleAdapter.listDailyPlansForMonth.removeAt(position)
                        scheduleAdapter.notifyItemRemoved(position)
                        scheduleAdapter.notifyDataSetChanged()
                        AlarmScheduler.removeAlarmsForDailyPlaner(context!!, dailyPlanId)
                    } else {
                        Toast.makeText(context, "Something went wrong when deleting", Toast.LENGTH_SHORT).show()
                    }
                }, {})
            }
            .setNegativeButton("No") { _, _ ->
                //do nothing
            }

        val alert = builder.create()
        alert.show()
    }

    override fun editDailyPlan(dailyPlan: DailyPlaner) {
        val direction = ScheduleFragmentDirections.openHome(dailyPlan.id)
        findNavController().navigate(direction)

    }
}