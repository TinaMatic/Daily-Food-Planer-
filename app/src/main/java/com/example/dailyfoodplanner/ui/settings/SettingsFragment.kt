package com.example.dailyfoodplanner.ui.settings


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.ui.login.LoginActivity
import com.jakewharton.rxbinding2.view.clicks
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var settingsViewModel: SettingsViewModel

    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        loadUsername()

        compositeDisposable.add(tvLogout.clicks().subscribe {
            settingsViewModel.logout()
            openLoginScreen()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun loadUsername(){
        compositeDisposable.add(settingsViewModel.getUsername()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tvUsername.text = it.capitalize()
            })
    }

    private fun openLoginScreen(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }

}
