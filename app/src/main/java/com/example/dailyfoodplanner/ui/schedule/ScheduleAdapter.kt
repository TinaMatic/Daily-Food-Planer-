package com.example.dailyfoodplanner.ui.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.convertDateToCalendarObject
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.getDayOfWeek
import kotlinx.android.synthetic.main.schedule_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAdapter (val context: Context, val listDailyPlansForMonth: ArrayList<DailyPlaner>)
    : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.schedule_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listDailyPlansForMonth.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listDailyPlansForMonth[position], position)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(dailyPlan: DailyPlaner, position: Int){
            val dayOfWeekNum = convertDateToCalendarObject(dailyPlan.date).get(Calendar.DAY_OF_WEEK)
            val dayOfWeek = getDayOfWeek(dayOfWeekNum)

            itemView.tvDate.text = "$dayOfWeek, ${dailyPlan.date}"

            itemView.tvBreakfastTime.text = dailyPlan.timeBreakfast
            itemView.tvBreakfastRecipe.text = dailyPlan.recipeBreakfast

            itemView.tvSnack1Time.text = dailyPlan.timeSnack1
            itemView.tvSnack1Recipe.text = dailyPlan.recipeSnack1

            itemView.tvLunchTime.text = dailyPlan.timeLunch
            itemView.tvLunchRecipe.text = dailyPlan.recipeLunch

            itemView.tvSnack2Time.text = dailyPlan.timeSnack2
            itemView.tvSnack2Recipe.text = dailyPlan.recipeSnack2

            itemView.tvDinnerTime.text = dailyPlan.timeDinner
            itemView.tvDinnerRecipe.text = dailyPlan.recipeDinner

            itemView.btnDeleteSchedule.setOnClickListener {
                onItemClickListener?.deleteDailyPlan(dailyPlan.id!!, position)
            }

            itemView.btnEditSchedule.setOnClickListener {
                onItemClickListener?.editDailyPlan(dailyPlan)
            }
        }
    }

    interface OnItemClickListener{
        fun deleteDailyPlan(dailyPlanId: String, position: Int)
        fun editDailyPlan(dailyPlan: DailyPlaner)
    }


}