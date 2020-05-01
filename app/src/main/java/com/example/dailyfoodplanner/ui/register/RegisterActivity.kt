package com.example.dailyfoodplanner.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.ui.main.MainActivity
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : DaggerAppCompatActivity() {
//
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var registerViewModel: RegisterViewModel

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        addTextChangeListener()

        compositeDisposable.add(btnRegister.clicks().subscribe {
            if(checkIfAllDataIsFilled()){
                createUserAccount()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun createUserAccount(){
        val username = etUsernameRegister.text.toString()
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()

        compositeDisposable.add(registerViewModel.registerUser(email, password, username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(it){
                    loginIntoApp()
                }  else{
                    Toast.makeText(this, "Something when wrong", Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun checkIfAllDataIsFilled(): Boolean{
        var isValid = true

        if(etUsernameRegister.text.toString().isEmpty()){
            textInputUsernameRegister.error = "Username is mandatory"
            isValid = false
        } else{
            textInputUsernameRegister.isErrorEnabled = false
        }

        if(etEmailRegister.text.toString().isEmpty()){
            textInputEmailRegister.error = "Email is mandatory"
            isValid = false
        } else{
            textInputEmailRegister.isErrorEnabled = false
        }

        if(etPasswordRegister.text.toString().isEmpty()){
            textInputPasswordRegister.error = "Password is mandatory"
            isValid = false
        } else{
            textInputPasswordRegister.isErrorEnabled = false
        }

        return isValid
    }

    private fun addTextChangeListener(){
        etUsernameRegister.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputUsernameRegister.error = ""
                } else{
                    textInputUsernameRegister.error = "Username is mandatory"
                }
            }
            .subscribe()

        etEmailRegister.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputEmailRegister.error = ""
                }
            }
            .subscribe()

        etPasswordRegister.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputPasswordRegister.error = ""
                }
            }
            .subscribe()
    }

    private fun loginIntoApp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
