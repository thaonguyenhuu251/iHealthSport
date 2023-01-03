package com.htnguyen.ihealth.view.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityProfileEditBinding
import com.htnguyen.ihealth.inter.OnDialog
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.DateDialog
import com.htnguyen.ihealth.view.component.LoadingDialog
import com.htnguyen.ihealth.view.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileEditActivity :
    BaseActivity<ActivityProfileEditBinding, ProfileEditViewModel>(),
    View.OnTouchListener,
    OnScrollChangedListener,
    OnDialog {

    override val layout: Int
        get() = R.layout.activity_profile_edit

    override val viewModel: ProfileEditViewModel by viewModel()

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    val db = Firebase.firestore

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindHideKeyboardListener(binding.root, binding.content)
        loadingDialog = LoadingDialog(this)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        binding.imgNext.setOnClickListener {
            db.collection("user").document(intent.getStringExtra(Constant.USER_ID ?: "").toString())
                .update(
                    mapOf(
                        "name" to viewModel.name.value.toString(),
                        "birthDay" to viewModel.birthDayLong.value,
                        "gender" to viewModel.gender.value,
                        "height" to viewModel.progressHeight.value?.toFloat(),
                        "weight" to viewModel.progressWeight.value?.toFloat()
                    )
                )
                .addOnSuccessListener {
                    PreferencesUtil.userName = viewModel.name.value.toString()
                    PreferencesUtil.userBirthDay = viewModel.birthDayLong.value
                    PreferencesUtil.userGender = viewModel.gender.value!!
                    PreferencesUtil.userHeight = viewModel.progressHeight.value?.toFloat()
                    PreferencesUtil.userWeight = viewModel.progressWeight.value?.toFloat()
                    loadingDialog?.dismissDialog()
                    val intent = Intent(this@ProfileEditActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    loadingDialog?.dismissDialog()
                    startActivity(Intent(this@ProfileEditActivity, MainActivity::class.java))
                    finish()
                }

        }

        binding.edtBirthDay.setOnClickListener {
            val dialog = DateDialog().newInstance(viewModel.birthDayLong.value!!)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        binding.scrollviewProfile.setOnTouchListener(this)
        binding.scrollviewProfile.viewTreeObserver.addOnScrollChangedListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    override fun onScrollChanged() {
        val view = binding.scrollviewProfile.getChildAt(binding.scrollviewProfile.childCount - 1)
        val topDetector = binding.scrollviewProfile.scrollY
        val bottomDetector: Int =
            view.bottom - (binding.scrollviewProfile.height + binding.scrollviewProfile.scrollY)
        if (bottomDetector == 0) {
        } else {
        }
    }

    override fun onClickOk(date: Long) {
        viewModel.birthDayLong.value = date
        viewModel.birthDay.value = SimpleDateFormat(getString(R.string.common_format_date))
            .format(viewModel.birthDayLong.value)
            .toString()
    }

}