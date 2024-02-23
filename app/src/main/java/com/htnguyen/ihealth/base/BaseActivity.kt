package com.htnguyen.ihealth.base

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htnguyen.ihealth.support.InputMethodManager
import com.htnguyen.ihealth.support.hideKeyboard
import com.htnguyen.ihealth.util.*
import com.htnguyen.ihealth.view.IHealthApplication
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    abstract val layout: Int
    lateinit var binding: T
    abstract val viewModel: R

    companion object {
        var dLocale: Locale? = null

    }

    var localeUpdatedContext: ContextWrapper? = null
    private var disposable: Disposable? = null

    override fun onConfigurationChanged(newConfig: Configuration) {
        newConfig.setLocale(dLocale)
        newConfig.setLayoutDirection(dLocale)
        super.onConfigurationChanged(newConfig)

    }

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
        disposable = IHealthApplication.eventBus.subscribe {
            it[Event.EVENT_CHANGE_LANGUAGE]?.let {
                onRestart()
            }
        }
    }

    fun bindHideKeyboardListener(vararg view: View) {
        val input = InputMethodManager(this) ?: return
        for (v in view) {
            v.setOnClickListener { input.hideKeyboard(it) }
            v.isClickable = true
        }
    }

    abstract fun getBindingVariable(): Int


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

    fun checkPermissionCamera() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.READ_MEDIA_IMAGES)
            .onSuccess {
                requestLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            .onDenied {
                checkPermissionCamera()
            }
            .onNeverAskAgain {
            }
            .run()
    }

    protected fun dispatchTakePictureIntent() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.READ_MEDIA_IMAGES)
            .onSuccess {
                requestLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            .onDenied {
                dispatchTakePictureIntent()
            }
            .onNeverAskAgain {
            }
            .run()
    }

    fun goToGallery() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.READ_MEDIA_IMAGES)
            .onSuccess {
                requestLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            .onDenied {
                goToGallery()
            }
            .onNeverAskAgain {

            }
            .run()
    }

    fun checkRecognition() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.ACTIVITY_RECOGNITION)
            .onSuccess {
                requestLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
            .onDenied {
                checkRecognition()
            }
            .onNeverAskAgain {

            }
            .run()
    }

    override fun onResume() {
        super.onResume()
        disposable = IHealthApplication.eventBus.subscribe{

            it[Event.EVENT_CHANGE_LANGUAGE]?.let {

            }
        }

    }


    override fun attachBaseContext(newBase: Context) {
        val change = when (PreferencesUtil.language) {
            Constant.LG_VIETNAMESE -> {
                "vi"
            }
            Constant.LG_ENGLISH -> {
                "en"
            }
            Constant.LG_RUSSIAN -> {
                "ru"
            }
            Constant.LG_LAOS -> {
                "lo"
            }
            Constant.LG_THAI -> {
                "th"
            }
            Constant.LG_KOREAN -> {
                "ko"
            }
            Constant.LG_CHINESE -> {
                "zh"
            }
            Constant.LG_JAPANESE -> {
                "ja"
            }
            Constant.LG_INDONESIAN -> {
                "in"
            }
            Constant.LG_SPANISH -> {
                "es"
            }
            Constant.LG_FRENCH -> {
                "fr"
            }
            Constant.LG_INDIAN -> {
                "kn"
            }
            Constant.LG_GERMAN -> {
                "de"
            }
            Constant.LG_ITALIAN -> {
                "it"
            }
            else -> {
                "en"
            }
        }
        dLocale = Locale(change)
        localeUpdatedContext = ContextUtils.updateLocale(newBase, dLocale!!)
        super.attachBaseContext(localeUpdatedContext)
    }

}