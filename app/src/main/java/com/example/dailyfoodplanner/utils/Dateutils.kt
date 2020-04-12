package com.example.dailyfoodplanner.utils

import java.text.SimpleDateFormat
import java.util.*

class Dateutils {

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val TIME_FORMAT = "HH:mm"

        fun convertDateToCalendarObject(date: String): Calendar{
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.GERMANY)
            var dateObj: Date
            val calendar = Calendar.getInstance()
            dateObj = sdf.parse(date)
            calendar.time = dateObj

            return calendar
        }

        fun getDayOfWeek(dayOfWeekNum: Int): String{
            return when(dayOfWeekNum){
                1 -> "Monday"
                2-> "Tuesday"
                3-> "Wednesday"
                4-> "Thursday"
                5-> "Friday"
                6-> "Saturday"
                7-> "Sunday"
                else -> ""
            }
        }

    }
}