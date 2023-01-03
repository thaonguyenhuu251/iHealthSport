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
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog
import com.htnguyen.ihealth.view.component.OTPVerificationDialog
import com.htnguyen.ihealth.view.main.MainActivity
import com.htnguyen.ihealth.view.profile.ProfileEditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>(){
    override val layout: Int
        get() = R.layout.activity_register

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override val viewModel: RegisterViewModel by viewModel()

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
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
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){

        } else {

        }
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
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userNew = User(
                                idAccount = viewModel.email.value.toString(),
                                passWord = viewModel.password.value.toString()
                            )

                            db.collection("user").document(email).set(userNew)
                                .addOnSuccessListener {
                                    loadingDialog?.dismissDialog()
                                    val intent = Intent(this@RegisterActivity, ProfileEditActivity::class.java)
                                    intent.putExtra(Constant.USER_ID, email)
                                    PreferencesUtil.idUser = email
                                    PreferencesUtil.passWord = password
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
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            //Log.d(TAG, "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //Log.w(TAG, "onVerificationFailed", e)
            loadingDialog?.dismissDialog()
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            loadingDialog?.dismissDialog()
            val dialog = OTPVerificationDialog().newInstance(
                "+84" + viewModel.phone.value.toString(),
                viewModel.password.value.toString(),
                verificationId
            )
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, dialog.tag)
            //resendToken = token
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}