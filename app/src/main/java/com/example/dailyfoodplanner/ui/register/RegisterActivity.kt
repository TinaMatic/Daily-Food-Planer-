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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var registerViewModel: RegisterViewModel

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = getString(R.string.register)

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

        if(isPasswordLongEnough(password)){
            compositeDisposable.add(registerViewModel.registerUser(email, password, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it){
                        loginIntoApp()
                    }  else{
                        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun isPasswordLongEnough(password: String): Boolean{
        return if(password.length >= 6){
            textInputPasswordRegister.isErrorEnabled = false
            true
        } else {
            textInputPasswordRegister.error = getString(R.string.password_not_long_enough)
            false
        }
    }

    private fun checkIfAllDataIsFilled(): Boolean{
        var isValid = true

        if(etUsernameRegister.text.toString().isEmpty()){
            textInputUsernameRegister.error = getString(R.string.username_mandatory)
            isValid = false
        } else{
            textInputUsernameRegister.isErrorEnabled = false
        }

        if(etEmailRegister.text.toString().isEmpty()){
            textInputEmailRegister.error = getString(R.string.email_mandatory)
            isValid = false
        } else{
            textInputEmailRegister.isErrorEnabled = false
        }

        if(etPasswordRegister.text.toString().isEmpty()){
            textInputPasswordRegister.error = getString(R.string.password_mandatory)
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
                    textInputUsernameRegister.error = getString(R.string.username_mandatory)
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
