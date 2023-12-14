package com.htnguyen.ihealth.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityRegisterBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.model.UserLogin
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog
import com.htnguyen.ihealth.view.component.OTPVerificationDialog
import com.htnguyen.ihealth.view.main.MainActivity
import com.htnguyen.ihealth.view.profile.ProfileEditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>(){
    override val layout: Int
        get() = R.layout.activity_register

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override val viewModel: RegisterViewModel by viewModel()

    var storedVerificationId : String? = null
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindHideKeyboardListener(binding.root, binding.content)

        registerWithEmail()
        binding.rgOptionRegister.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rdEmail) {
                registerWithEmail()
            } else {
                registerWithPhoneNumber()
            }
        }

        binding.edtPassword.doAfterTextChanged {
            viewModel.isValidatePasswordMatch()
            binding.tvRegister.isEnabled = viewModel.isPasswordNotEmpty()
        }

        binding.edtConfirmPassword.doAfterTextChanged {
            viewModel.isValidatePasswordMatch()
            binding.tvRegister.isEnabled = viewModel.isPasswordNotEmpty()
        }

        binding.lnHaveAccount.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
        loadingDialog = LoadingDialog(this)
    }

    private fun registerWithEmail() {
        binding.edtEmail.visibility = View.VISIBLE
        binding.lnPhoneNumber.visibility = View.GONE
        viewModel.phone.value = null
        viewModel.password.value = null
        viewModel.confirmPassword.value = null
        binding.tvRegister.setOnClickListener {
            if (viewModel.isValidatePasswordFinish() && viewModel.isEmailEmpty()) {
                loadingDialog?.showDialog()
                val email = viewModel.email.value.toString()
                val password = viewModel.password.value.toString()
                FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val idUser = task.result.user?.uid ?: "user" + Calendar().timeInMillis.toString() + Random.nextInt(0, 999).toString()
                            val userLogin = UserLogin(
                                idUser = idUser,
                                account = viewModel.email.value.toString(),
                                password = viewModel.password.value.toString()
                            )

                            FirebaseUtils.db.collection("UserLogin").document(viewModel.email.value.toString()).set(userLogin)
                                .addOnSuccessListener {
                                    loadingDialog?.dismissDialog()
                                    PreferencesUtil.idUser = idUser
                                    PreferencesUtil.account = email
                                    PreferencesUtil.passWord = password
                                    val intent = Intent(this@RegisterActivity, ProfileEditActivity::class.java)
                                    intent.putExtra(Constant.USER_ID, idUser)
                                    intent.putExtra(Constant.USER_ACCOUNT, email)
                                    intent.putExtra(Constant.TYPE_PROFILE, 0)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener{ e ->
                                    loadingDialog?.dismissDialog()
                                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                }

                        } else {
                            loadingDialog?.dismissDialog()
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }

    private fun registerWithPhoneNumber() {
        binding.edtEmail.visibility = View.GONE
        binding.lnPhoneNumber.visibility = View.VISIBLE
        viewModel.email.value = null
        viewModel.password.value = null
        viewModel.confirmPassword.value = null
        binding.tvRegister.setOnClickListener {
            if (viewModel.isValidatePasswordFinish() && viewModel.isPhoneEmpty() ) {
                loadingDialog?.showDialog()
                sendVerificationCode("+84" + viewModel.phone.value.toString())
            }
        }
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {
            loadingDialog?.dismissDialog()
            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {

            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            loadingDialog?.dismissDialog()
            val dialog = OTPVerificationDialog().newInstance(
                "+84" + viewModel.phone.value.toString(),
                viewModel.password.value.toString(),
                verificationId
            )
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseUtils.firebaseAuth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}