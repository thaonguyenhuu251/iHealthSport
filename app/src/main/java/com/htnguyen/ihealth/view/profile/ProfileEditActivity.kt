package com.htnguyen.ihealth.view.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.TutorialActivity
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityProfileEditBinding
import com.htnguyen.ihealth.inter.OnDialog
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.model.UserLogin
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.DateDialog
import com.htnguyen.ihealth.view.component.LoadingDialog
import com.htnguyen.ihealth.view.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


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

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindHideKeyboardListener(binding.root, binding.content)
        loadingDialog = LoadingDialog(this)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        viewModel.name.value = PreferencesUtil.userName
        viewModel.birthDayLong.value = PreferencesUtil.userBirthDay
        viewModel.gender.value = PreferencesUtil.userGender
        viewModel.progressHeight.value = PreferencesUtil.userHeight?.roundToInt()
        viewModel.progressWeight.value = PreferencesUtil.userWeight?.roundToInt()
        viewModel.birthDay.value = SimpleDateFormat("dd/MM/yyyy")
            .format(viewModel.birthDayLong.value)
            .toString()

        if (intent != null) {
            if (intent.getIntExtra(Constant.TYPE_PROFILE, 1) == 0) {
                binding.imgNext.visibility = View.VISIBLE
                binding.imgBack.visibility = View.GONE
                binding.txtSave.visibility = View.GONE
            } else {
                binding.imgNext.visibility = View.GONE
                binding.imgBack.visibility = View.VISIBLE
                binding.txtSave.visibility = View.VISIBLE
            }
        }
        binding.imgNext.setOnClickListener {
            addProfileUser()
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.txtSave.setOnClickListener {
            updateProfileUser()
        }

        binding.edtBirthDay.setOnClickListener {
            val dialog = DateDialog().newInstance(viewModel.birthDayLong.value!!)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            viewModel.gender.value = checkedId == R.id.rdMale
        }

        binding.scrollviewProfile.setOnTouchListener(this)
        binding.scrollviewProfile.viewTreeObserver.addOnScrollChangedListener(this)
    }

    private fun addProfileUser() {
        val idUser = intent.getStringExtra(Constant.USER_ID) ?: PreferencesUtil.idUser
        val user = User(
            idUser = intent.getStringExtra(Constant.USER_ID),
            email = "",
            phoneNumber = "",
            birthDay = viewModel.birthDayLong.value,
            gender = viewModel.gender.value,
            height = viewModel.progressHeight.value?.toFloat(),
            weight = viewModel.progressWeight.value?.toFloat(),
            name = viewModel.name.value.toString()
        )

        FirebaseUtils.db.collection("User").document().set(user)
            .addOnSuccessListener {
                PreferencesUtil.userName = viewModel.name.value.toString()
                PreferencesUtil.userBirthDay = viewModel.birthDayLong.value
                PreferencesUtil.userGender = viewModel.gender.value!!
                PreferencesUtil.userHeight = viewModel.progressHeight.value?.toFloat()
                PreferencesUtil.userWeight = viewModel.progressWeight.value?.toFloat()
                loadingDialog?.dismissDialog()
                val intent = if (PreferencesUtil.isAgreedTerms) {
                    Intent(this@ProfileEditActivity, MainActivity::class.java)
                } else {
                    Intent(this@ProfileEditActivity, TutorialActivity::class.java)
                }

                startActivity(intent)
                finish()
            }
            .addOnFailureListener{ e ->
                loadingDialog?.dismissDialog()
                finish()
            }
    }

    private fun updateProfileUser() {
        val idUser = intent.getStringExtra(Constant.USER_ID) ?: PreferencesUtil.idUser
        val user = User(
            idUser = intent.getStringExtra(Constant.USER_ID),
            email = "",
            phoneNumber = "",
            birthDay = viewModel.birthDayLong.value,
            gender = viewModel.gender.value,
            height = viewModel.progressHeight.value?.toFloat(),
            weight = viewModel.progressWeight.value?.toFloat(),
            name = viewModel.name.value.toString()
        )

        FirebaseUtils.db.collection("User")
            .document(PreferencesUtil.idUser!!)
            .update(
                mapOf(
                    "name" to viewModel.name.value.toString(),
                    "birthDay" to viewModel.birthDayLong.value,
                    "gender" to viewModel.gender.value!!,
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
                val intent = if (PreferencesUtil.isAgreedTerms) {
                    Intent(this@ProfileEditActivity, MainActivity::class.java)
                } else {
                    Intent(this@ProfileEditActivity, TutorialActivity::class.java)
                }

                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                loadingDialog?.dismissDialog()
                finish()
            }
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
        viewModel.birthDay.value = SimpleDateFormat("dd/MM/yyyy")
            .format(viewModel.birthDayLong.value)
            .toString()
    }

}