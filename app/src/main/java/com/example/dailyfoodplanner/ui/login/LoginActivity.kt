package com.example.dailyfoodplanner.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.data.FirebaseRepository
import com.example.dailyfoodplanner.ui.main.MainActivity
import com.example.dailyfoodplanner.ui.register.RegisterActivity
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var loginViewModel: LoginViewModel

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = getString(R.string.login)

        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        compositeDisposable.add(loginViewModel.isUserLoggedIn()
            .subscribe {
                if(it){
                    loginIntoApp()
                }
            })

        addTextChangeListener()

        compositeDisposable.add(btnLogin.clicks().subscribe({
            if(checkIfAllDataIsFilled()){
                login()
            }
        },{}))

        compositeDisposable.add(tvRegister.clicks().subscribe ({
            openRegisterScreen()
        },{
            Log.d("Error open register", it.toString())
        }))
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.getFirebaseAuth().addAuthStateListener(loginViewModel.getAuthListener()!!)
    }

    override fun onStop() {
        super.onStop()
        if(loginViewModel.getFirebaseAuth() != null){
            loginViewModel.getFirebaseAuth().removeAuthStateListener(loginViewModel.getAuthListener()!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    fun login(){
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()

        compositeDisposable.add(loginViewModel.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(it){
                    loginIntoApp()
                } else{
                    Toast.makeText(this, getString(R.string.user_does_not_exist), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun checkIfAllDataIsFilled(): Boolean{
        var isValid = true

        if(etEmailLogin.text.toString().isEmpty()){
            textInputEmailLogin.error = getString(R.string.email_mandatory)
            isValid = false
        } else{
            textInputEmailLogin.isErrorEnabled = false
        }

        if(etPasswordLogin.text.toString().isEmpty()){
            textInputPasswordLogin.error = getString(R.string.password_mandatory)
            isValid = false
        } else{
            textInputPasswordLogin.isErrorEnabled = false
        }

        return isValid
    }

    private fun addTextChangeListener(){

        etEmailLogin.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputEmailLogin.error = ""
                }
            }
            .subscribe()

        etPasswordLogin.afterTextChangeEvents()
            .map {
                it.toString()
            }
            .doOnNext {
                if(it.isNotEmpty()){
                    textInputPasswordLogin.error = ""
                }
            }
            .subscribe()
    }

    private fun openRegisterScreen(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loginIntoApp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
