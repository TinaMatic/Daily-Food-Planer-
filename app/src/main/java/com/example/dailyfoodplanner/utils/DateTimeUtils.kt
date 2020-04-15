package com.example.dailyfoodplanner.utils

import com.example.dailyfoodplanner.model.DailyPlaner
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

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