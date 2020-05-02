package com.example.dailyfoodplanner.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.notification.AlarmScheduler
import com.example.dailyfoodplanner.ui.main.MainActivity
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.DATE_FORMAT
import com.example.dailyfoodplanner.utils.DateTimeUtils.Companion.TIME_FORMAT
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : DaggerFragment(), View.OnClickListener, View.OnFocusChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var adapter: ArrayAdapter<String>

    private var listOfRecipes = arrayListOf("None")

    var compositeDisposable = CompositeDisposable()

    private var datePicker: DatePickerDialog? = null

    private var timePicker: TimePickerDialog? = null

    private val cal = Calendar.getInstance()

    private var editDate: String? = null

    private var editTimeBreakfast: String? = null

    private var editTimeSnack1: String? = null

    private var editTimeLunch: String? = null

    private var editTimeSnack2: String? = null

    private var editTimeDinner: String? = null

    var dailyPlanId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        dailyPlanId = HomeFragmentArgs.fromBundle(arguments!!).dailyPlanId

        setInitials(dailyPlanId)

        addTextChangeListener()

        compositeDisposable.add(btnEdit.clicks().subscribe {
            if(ifAllDataIsFilled()){
                editDailyPlan(dailyPlanId!!)
                etTimeDinner.clearFocus()
            }
        })

        compositeDisposable.add(btnAdd.clicks().subscribe {
            if (ifAllDataIsFilled()){
                addDailyPlaner()
                etTimeDinner.clearFocus()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        //set today as default
        val today = SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().time)
        textInputDate.hint =  "Today, $today"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
        homeViewModel.claer()

        (activity as MainActivity).shouldEnableBottomNavigation(true)
    }

    private fun setInitials(dailyPlanId: String?){
        etDate.showSoftInputOnFocus = false
        loadAllRecipes()

        adapter = ArrayAdapter(requireContext(), R.layout.list_recipes, listOfRecipes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerBreakfast.adapter = adapter
        spinnerSnack1.adapter = adapter
        spinnerLunch.adapter = adapter
        spinnerSnack2.adapter = adapter
        spinnerDinner.adapter = adapter

        //show correct button
        if(dailyPlanId.isNullOrEmpty()){
            btnAdd.visibility = View.VISIBLE
            btnEdit.visibility = View.INVISIBLE
        } else {
            (activity as MainActivity).displayHomeButton()
            (activity as MainActivity).setActionBarTitle(getString(R.string.title_schedule_edit))
            (activity as MainActivity).shouldEnableBottomNavigation(false)

            loadSingleDailyPlan(dailyPlanId)

            btnAdd.visibility = View.INVISIBLE
            btnEdit.visibility = View.VISIBLE
        }

        //set today as default
        val today = SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().time)
        textInputDate.hint =  "Today, $today"
        etDate.setText( "Today, $today")

        //set onClick listeners
        etDate.setOnClickListener(this)
        etTimeBreakfast.setOnClickListener(this)
        etTimeSnack1.setOnClickListener(this)
        etTimeLunch.setOnClickListener(this)
        etTimeSnack2.setOnClickListener(this)
        etTimeDinner.setOnClickListener(this)

        //set onFocus listeners
        etDate.onFocusChangeListener = this
        etTimeBreakfast.onFocusChangeListener = this
        etTimeSnack1.onFocusChangeListener = this
        etTimeLunch.onFocusChangeListener = this
        etTimeSnack2.onFocusChangeListener = this
        etTimeDinner.onFocusChangeListener = this
    }

    private fun loadAllRecipes(){
        homeViewModel.loadAllRecipes()

        homeViewModel.recipeLiveData.observe(this, androidx.lifecycle.Observer {listRecipes->
            listRecipes.forEach {
                listOfRecipes.add(it.title)
            }
        })
    }

    private fun openDatePicker(viewId: Int){
        val editText = view?.findViewById<EditText>(viewId)
        (activity as MainActivity).hideKeyboard(editText as View)

        val yearDisplay: Int
        val monthDisplay: Int
        val dayDisplay: Int

        if(editDate != null){
            yearDisplay = editDate?.substring(6)!!.toInt()
            monthDisplay = editDate?.substring(3,5)!!.toInt()
            dayDisplay = editDate?.substring(0,2)!!.toInt()
        } else{
            yearDisplay = cal.get(Calendar.YEAR)
            monthDisplay = cal.get(Calendar.MONTH)
            dayDisplay = cal.get(Calendar.DAY_OF_MONTH)
        }

        datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            editText.setText(SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(cal.time))
        }, yearDisplay, monthDisplay, dayDisplay)

        datePicker?.show()
        datePicker?.datePicker?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun openTimePicker(viewId: Int){
        val editText = view?.findViewById<EditText>(viewId)

        val hourDisplay = getHour(viewId)
        val minuteDisplay = getMinute(viewId)

        timePicker = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            editText?.setText(SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(cal.time))
        }, hourDisplay, minuteDisplay, true)

        timePicker?.show()
    }

    private fun getHour(viewId: Int): Int{

        return when {
            editTimeBreakfast != null && viewId.equals(R.id.etTimeBreakfast) -> {
                editTimeBreakfast?.substring(0,2)!!.toInt()
            }
            editTimeSnack1 != null && viewId.equals(R.id.etTimeSnack1) -> {
                editTimeSnack1?.substring(0,2)!!.toInt()
            }
            editTimeLunch != null && viewId.equals(R.id.etTimeLunch) ->{
                editTimeLunch?.substring(0,2)!!.toInt()
            }
            editTimeSnack2 != null && viewId.equals(R.id.etTimeSnack2) -> {
                editTimeSnack2?.substring(0,2)!!.toInt()
            }
            editTimeDinner != null && viewId.equals(R.id.etTimeDinner) -> {
                editTimeDinner?.substring(0,2)!!.toInt()
            }
            else -> {
                cal.get(Calendar.HOUR_OF_DAY)
            }
        }
    }

    private fun getMinute(viewId: Int): Int{
        return when {
            editTimeBreakfast != null && viewId.equals(R.id.etTimeBreakfast) -> {
                editTimeBreakfast?.substring(3)!!.toInt()
            }
            editTimeSnack1 != null && viewId.equals(R.id.etTimeSnack1) -> {
                editTimeSnack1?.substring(3)!!.toInt()
            }
            editTimeLunch != null && viewId.equals(R.id.etTimeLunch) ->{
                editTimeLunch?.substring(3)!!.toInt()
            }
            editTimeSnack2 != null && viewId.equals(R.id.etTimeSnack2) -> {
                editTimeSnack2?.substring(3)!!.toInt()
            }
            editTimeDinner != null && viewId.equals(R.id.etTimeDinner) -> {
                editTimeDinner?.substring(3)!!.toInt()
            }
            else -> {
                cal.get(Calendar.MINUTE)
            }
        }
    }

    private fun addDailyPlaner(){
        val dailyPlaner = DailyPlaner(null, etDate.text.toString(), etTimeBreakfast.text.toString(), spinnerBreakfast.selectedItem.toString(),
            etTimeSnack1.text.toString(), spinnerSnack1.selectedItem.toString(), etTimeLunch.text.toString(), spinnerLunch.selectedItem.toString(),
            etTimeSnack2.text.toString(), spinnerSnack2.selectedItem.toString(), etTimeDinner.text.toString(), spinnerDinner.selectedItem.toString())
        val date = etDate.text.toString()

        compositeDisposable.add(homeViewModel.addDailyPlaner(dailyPlaner).subscribe { (isSuccessfull, dailyPlan) ->
            if (isSuccessfull){
                Toast.makeText(context, "Daily plan was successfully added", Toast.LENGTH_SHORT).show()
                AlarmScheduler.scheduleAlarmForDailyPlaner(context!!, dailyPlan!!)
                cleanAllFields()
            } else if (!isSuccessfull && dailyPlan != null){
                Toast.makeText(context, "Daily Plan with $date date already exists", Toast.LENGTH_LONG).show()
            } else if(!isSuccessfull && dailyPlan == null){
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editDailyPlan(dailyPlanId: String){
        val dailyPlaner = DailyPlaner(dailyPlanId, etDate.text.toString(), etTimeBreakfast.text.toString(), spinnerBreakfast.selectedItem.toString(),
            etTimeSnack1.text.toString(), spinnerSnack1.selectedItem.toString(), etTimeLunch.text.toString(), spinnerLunch.selectedItem.toString(),
            etTimeSnack2.text.toString(), spinnerSnack2.selectedItem.toString(), etTimeDinner.text.toString(), spinnerDinner.selectedItem.toString())

        compositeDisposable.add(homeViewModel.editDailyPlan(dailyPlaner).subscribe {
            if(it){
                Toast.makeText(context, "Daily plan was successfully updated", Toast.LENGTH_SHORT).show()
                AlarmScheduler.scheduleAlarmForDailyPlaner(context!!, dailyPlaner)
            } else{
                Toast.makeText(context, "Something went wrong when editing", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadSingleDailyPlan(dailyPlanId: String){
        homeViewModel.readSingleDailyPlan(dailyPlanId)

        homeViewModel.dailyPlanLiveData.observe(this, androidx.lifecycle.Observer {
            etDate.setText(it.date)
            etTimeBreakfast.setText(it.timeBreakfast)
            etTimeSnack1.setText(it.timeSnack1)
            etTimeLunch.setText(it.timeLunch)
            etTimeSnack2.setText(it.timeSnack2)
            etTimeDinner.setText(it.timeDinner)

            //get all the times and dates
            editDate = it.date
            editTimeBreakfast = it.timeBreakfast
            editTimeSnack1 = it.timeSnack1
            editTimeLunch = it.timeLunch
            editTimeSnack2 = it.timeSnack2
            editTimeDinner = it.timeDinner

            //set spinner
            spinnerBreakfast.setSelection(adapter.getPosition(it.recipeBreakfast))
            spinnerSnack1.setSelection(adapter.getPosition(it.recipeSnack1))
            spinnerLunch.setSelection(adapter.getPosition(it.recipeLunch))
            spinnerSnack2.setSelection(adapter.getPosition(it.recipeSnack2))
            spinnerDinner.setSelection(adapter.getPosition(it.recipeDinner))
        })
    }

    private fun cleanAllFields(){
        etDate.setText("")
        etTimeBreakfast.setText("")
        etTimeSnack1.setText("")
        etTimeLunch.setText("")
        etTimeSnack2.setText("")
        etTimeDinner.setText("")
    }

    private fun ifAllDataIsFilled(): Boolean{
        var isValid = true

        if(etDate.text.toString().isEmpty()){
            textInputDate.error = "Date is mandatory"
            isValid = false
        } else{
            textInputDate.isErrorEnabled = false
        }

        if(etTimeBreakfast.text.toString().isEmpty()){
            textInputBreakfast.error = "Breakfast time is mandatory"
            isValid = false
        } else{
            textInputBreakfast.isErrorEnabled = false
        }

        if(etTimeSnack1.text.toString().isEmpty()){
            textInputSnack1.error = "Snack time is mandatory"
            isValid = false
        }  else{
            textInputSnack1.isErrorEnabled = false
        }

        if(etTimeLunch.text.toString().isEmpty()){
            textInputLunch.error = "Lunch time is mandatory"
            isValid = false
        } else{
            textInputLunch.isErrorEnabled = false
        }

        if(etTimeSnack2.text.toString().isEmpty()){
            textInputSnack2.error = "Snack time is mandatory"
            isValid = false
        } else{
            textInputSnack2.isErrorEnabled = false
        }

        if(etTimeDinner.text.toString().isEmpty()){
            textInputDinner.error = "Dinner time is mandatory"
            isValid = false
        } else{
            textInputDinner.isErrorEnabled = false
        }

        return isValid
    }

    private fun addTextChangeListener(){

        etDate.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputDate.error = ""
                } else{
                    textInputDate.error = "Date is mandatory"
                }
            }
            .subscribe()

        etTimeBreakfast.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputBreakfast.error = ""
                }
            }
            .subscribe()

        etTimeSnack1.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputSnack1.error = ""
                }
            }
            .subscribe()

        etTimeLunch.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputLunch.error = ""
                }
            }
            .subscribe()

        etTimeSnack2.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputSnack2.error = ""
                }
            }
            .subscribe()

        etTimeDinner.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputDinner.error = ""
                }
            }
            .subscribe()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.etDate -> openDatePicker(view.id)
            R.id.etTimeBreakfast -> openTimePicker(view.id)
            R.id.etTimeSnack1 -> openTimePicker(view.id)
            R.id.etTimeLunch -> openTimePicker(view.id)
            R.id.etTimeSnack2 -> openTimePicker(view.id)
            R.id.etTimeDinner -> openTimePicker(view.id)
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus){
            when(view?.id){
                R.id.etDate -> openDatePicker(view.id)
                R.id.etTimeBreakfast -> openTimePicker(view.id)
                R.id.etTimeSnack1 -> openTimePicker(view.id)
                R.id.etTimeLunch -> openTimePicker(view.id)
                R.id.etTimeSnack2 -> openTimePicker(view.id)
                R.id.etTimeDinner -> openTimePicker(view.id)
            }
        }
    }
}