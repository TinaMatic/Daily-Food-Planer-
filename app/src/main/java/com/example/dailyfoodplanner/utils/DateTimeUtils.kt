package com.example.dailyfoodplanner.utils

import com.example.dailyfoodplanner.model.DailyPlaner
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val TIME_FORMAT = "HH:mm"

        fun convertDateToCalendarObject(date: String): Calendar{
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.GERMANY)
            val dateObj: Date
            val calendar = Calendar.getInstance()
            dateObj = sdf.parse(date)
            calendar.time = dateObj

            return calendar
        }

        fun convertTimeToObject(time: String): Calendar{
            val sdf = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            val timeObj: Date
            val calendar = Calendar.getInstance()
            timeObj = sdf.parse(time)
            calendar.time = timeObj

            return calendar
        }

        fun getDayOfWeek(dayOfWeekNum: Int): String{
            return when(dayOfWeekNum){
                1-> "Sunday"
                2 -> "Monday"
                3-> "Tuesday"
                4-> "Wednesday"
                5-> "Thursday"
                6-> "Friday"
                7-> "Saturday"
                else -> ""
            }
        }

        fun getHourForMeal(dailyPlaner: DailyPlaner, typeOfMeal: String): Int{
            return when(typeOfMeal.toLowerCase()){
                "breakfast" -> dailyPlaner.timeBreakfast.substring(0,2).toInt()
                "snack1" -> dailyPlaner.timeSnack1.substring(0,2).toInt()
                "lunch" -> dailyPlaner.timeLunch.substring(0,2).toInt()
                "snack2" -> dailyPlaner.timeSnack2.substring(0,2).toInt()
                "dinner" -> dailyPlaner.timeDinner.substring(0,2).toInt()
                else -> 0
            }
        }

        fun getMinuteForMeail(dailyPlaner: DailyPlaner, typeOfMeal: String): Int{
            return when(typeOfMeal.toLowerCase()){
                "breakfast" -> dailyPlaner.timeBreakfast.substring(3).toInt()
                "snack1" -> dailyPlaner.timeSnack1.substring(3).toInt()
                "lunch" -> dailyPlaner.timeLunch.substring(3).toInt()
                "snack2" -> dailyPlaner.timeSnack2.substring(3).toInt()
                "dinner" -> dailyPlaner.timeDinner.substring(3).toInt()
                else -> 0
            }
        }


    }
}