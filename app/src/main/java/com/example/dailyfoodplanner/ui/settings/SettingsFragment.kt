package com.example.dailyfoodplanner.ui.settings


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider

import com.example.dailyfoodplanner.R
import com.example.dailyfoodplanner.constants.Constants.Companion.GALLERY_ID
import com.example.dailyfoodplanner.constants.Constants.Companion.PUSH_NOTIFICATIONS_ENABLED
import com.example.dailyfoodplanner.ui.login.LoginActivity
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
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

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        loadProifileImage()
        setSwitchState()

        compositeDisposable.add(tvLogout.clicks().subscribe {
            settingsViewModel.logout()
            openLoginScreen()
        })

        compositeDisposable.add(civProfile.clicks().subscribe {
            openGallery()
        })

    }

    override fun onResume() {
        super.onResume()
        setSwitchState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK){
            val image: Uri = data?.data!!

            val intent = CropImage.activity(image)
                .setAspectRatio(1,1)
                .getIntent(context!!)

            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
//            CropImage.activity(image)
//                .setAspectRatio(1,1)
//                .start(requireActivity())
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)

            if(resultCode == Activity.RESULT_OK){
                val resultUri = result.uri

                compositeDisposable.add(settingsViewModel.loadImage(resultUri).subscribe {
                    if (it){
                        Picasso.with(context)
                            .load(resultUri)
                            .placeholder(R.drawable.profile_img)
                            .into(civProfile)
                        Toast.makeText(context, "Picture successfully added", Toast.LENGTH_SHORT).show()
                    }
                })
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loadUsername(){
        compositeDisposable.add(settingsViewModel.getUsername()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tvUsername.text = it.capitalize()
            })
    }

    private fun loadProifileImage(){
        compositeDisposable.add(settingsViewModel.getProfileImage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Picasso.with(context)
                    .load(it)
                    .placeholder(R.drawable.profile_img)
                    .into(civProfile)
            })
    }

    private fun setSwitchState(){
        if (NotificationManagerCompat.from(context!!).areNotificationsEnabled()){
            notificationsSwitch.isChecked = sharedPreferences.getBoolean(PUSH_NOTIFICATIONS_ENABLED, true)
            notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    sharedPreferences.edit()
                        .putBoolean(PUSH_NOTIFICATIONS_ENABLED, true)
                        .apply()
                } else{
                    sharedPreferences.edit()
                        .putBoolean(PUSH_NOTIFICATIONS_ENABLED, false)
                        .apply()
                }
            }
        } else{
            notificationsSwitch.isChecked = false
            sharedPreferences.edit()
                .putBoolean(PUSH_NOTIFICATIONS_ENABLED, false)
                .apply()

            notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    showSettingsDialog()
                }
            }
        }
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context?.getString(R.string.notification_dialog_title))
            .setMessage(context?.getString(R.string.notification_dialog_message))
            .setPositiveButton("Yes"){dialog, which ->
                sharedPreferences.edit()
                    .putBoolean(PUSH_NOTIFICATIONS_ENABLED, true)
                    .apply()

                openNotificationsSettings()
            }.setNegativeButton("No"){dialog, which->
                notificationsSwitch.isChecked = false
                sharedPreferences.edit()
                    .putBoolean(PUSH_NOTIFICATIONS_ENABLED, false)
                    .apply()
            }

        val alert = builder.create()
        alert.show()
    }

    private fun openNotificationsSettings() {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->{
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity?.applicationContext?.packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ->{
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", activity?.applicationContext?.packageName)
                intent.putExtra("app_uid", activity?.applicationInfo?.uid)
            }
            else -> {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + activity?.applicationContext?.packageName)
            }
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("Settings", true)
        activity?.startActivity(intent)
    }

    private fun openLoginScreen(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun openGallery(){
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
    }

}
