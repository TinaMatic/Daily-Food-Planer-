package com.example.dailyfoodplanner.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.base.ViewModelFactory
import com.example.dailyfoodplanner.notification.AlarmReceiver
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class  MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController

    private var currentDestination: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_schedule,
                R.id.navigation_recipes,
                R.id.navigation_notes,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        currentDestination = R.id.navigation_home

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.navigation_recipe_details){
                navView.visibility = View.GONE
            } else{
                navView.visibility = View.VISIBLE
            }
        }

    }

    fun displayHomeButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    fun setActionBarTitle(title: String){
        supportActionBar?.title = title
    }

    fun shouldEnableBottomNavigation(enable: Boolean){
        for(i in 0 until nav_view.menu.size()){
            nav_view.menu.getItem(i).isEnabled = enable
        }
    }

    fun hideKeyboard(view: View){
        val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
