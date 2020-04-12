package com.example.dailyfoodplanner.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.model.DailyPlaner
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : DaggerFragment(), View.OnClickListener, View.OnFocusChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel

    var compositeDisposable = CompositeDisposable()

    private val cal = Calendar.getInstance()

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

        addTextChangeListner()

        val today = SimpleDateFormat("dd/MM/yy").format(Calendar.getInstance().time)
        textInputDate.hint =  "Today, $today"

        val items = listOf("Material", "Design", "None")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_recipes, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBreakfast.adapter = adapter
        spinnerSnack1.adapter = adapter
        spinnerLunch.adapter = adapter
        spinnerSnack2.adapter = adapter
        spinnerDinner.adapter = adapter

        etDate.setOnClickListener(this)
        etTimeBreakfast.setOnClickListener(this)
        etTimeSnack1.setOnClickListener(this)
        etTimeLunch.setOnClickListener(this)
        etTimeSnack2.setOnClickListener(this)
        etTimeDinner.setOnClickListener(this)

        etDate.setOnFocusChangeListener(this)
        etTimeBreakfast.onFocusChangeListener = this
        etTimeSnack1.onFocusChangeListener = this
        etTimeLunch.onFocusChangeListener = this
        etTimeSnack2.onFocusChangeListener = this
        etTimeDinner.onFocusChangeListener = this

        compositeDisposable.add(btnAdd.clicks().subscribe {
            if (ifAllDataIsFilled()){
                writeAllData()
                cleanAllFields()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }


    private fun openDatePicker(resources: Int){
        val editText = view?.findViewById<EditText>(resources)

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            editText?.setText("$dayOfMonth/$month/$year")
        }, year, month, day)
        datePicker.show()
    }

    private fun openTimePicker(resources: Int){
        val editText = view?.findViewById<EditText>(resources)

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            editText?.setText(SimpleDateFormat("HH:mm").format(cal.time))
        }, hour, minute, true)

        timePicker.show()
    }

    private fun writeAllData(){
        val dailyPlaner = DailyPlaner(null, etDate.text.toString(), etTimeBreakfast.text.toString(), spinnerBreakfast.selectedItem.toString(),
            etTimeSnack1.text.toString(), spinnerSnack1.selectedItem.toString(), etTimeLunch.text.toString(), spinnerLunch.selectedItem.toString(),
            etTimeSnack2.text.toString(), spinnerSnack2.selectedItem.toString(), etTimeDinner.text.toString(), spinnerDinner.selectedItem.toString())

        compositeDisposable.add(homeViewModel.writeDailyPlaner(dailyPlaner).subscribe {
            if (it){
                Toast.makeText(context, "Daily plan was successfully added", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
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

    private fun addTextChangeListner(){

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