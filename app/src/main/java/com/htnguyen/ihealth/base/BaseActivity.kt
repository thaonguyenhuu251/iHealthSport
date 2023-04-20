package com.htnguyen.ihealth.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htnguyen.ihealth.support.InputMethodManager
import com.htnguyen.ihealth.support.hideKeyboard
import com.htnguyen.ihealth.util.PermissionHelper
import com.htnguyen.ihealth.util.PreferencesUtil
import java.io.IOException

abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    abstract val layout: Int
    lateinit var binding: T
    abstract val viewModel: R

    private val permissionHelper: PermissionHelper by lazy {
        PermissionHelper()
    }
    var requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this@BaseActivity
        binding.setVariable(getBindingVariable(), viewModel)
    }

    fun bindHideKeyboardListener(vararg view: View) {
        val input = InputMethodManager(this) ?: return
        for (v in view) {
            v.setOnClickListener { input.hideKeyboard(it) }
            v.isClickable = true
        }
    }

    abstract fun getBindingVariable(): Int

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    protected fun checkNotification() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.POST_NOTIFICATIONS)
            .onSuccess {
                requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                PreferencesUtil.isNotification = true
                PreferencesUtil.isAnswerNotification = true
            }
            .onDenied {
                PreferencesUtil.isNotification = false
                PreferencesUtil.isAnswerNotification = true
            }
            .onNeverAskAgain {
            }
            .run()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermissionCamera() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.READ_MEDIA_IMAGES)
            .onSuccess {

            }
            .onDenied {
            }
            .onNeverAskAgain {
            }
            .run()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    protected fun dispatchTakePictureIntent() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.READ_MEDIA_IMAGES)
            .onSuccess {

            }
            .onDenied {
            }
            .onNeverAskAgain {
            }
            .run()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun goToGallery() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.READ_MEDIA_IMAGES)
            .onSuccess {
            }
            .onDenied {

            }
            .onNeverAskAgain {

            }
            .run()
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkRecognition() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.ACTIVITY_RECOGNITION)
            .onSuccess {
            }
            .onDenied {

            }
            .onNeverAskAgain {

            }
            .run()
    }



}