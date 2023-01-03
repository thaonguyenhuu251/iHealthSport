package com.htnguyen.ihealth.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.TutorialActivity
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityLoginBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog
import com.htnguyen.ihealth.view.main.MainActivity
import com.htnguyen.ihealth.view.profile.ProfileEditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override val layout: Int
        get() = R.layout.activity_login

    override val viewModel: LoginViewModel by viewModel()

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindHideKeyboardListener(binding.root, binding.content)
        loadingDialog = LoadingDialog(this)
        binding.lnNotAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
        actionMoveScreen()
    }

    private fun actionMoveScreen() {
        binding.tvSignIn.setOnClickListener {
            loadingDialog?.showDialog()
            loginWithAccountAndPassword()
        }
    }

    private fun loginWithAccountAndPassword() {
        val idAccount = viewModel.idAccount.value.toString()
        val password = viewModel.password.value.toString()
        FirebaseUtils.db.collection("user").document(idAccount).get()
            .addOnSuccessListener { document ->
                PreferencesUtil.idUser = idAccount
                PreferencesUtil.passWord = password
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    if (user?.passWord == PreferencesUtil.passWord) {
                        loadingDialog?.dismissDialog()
                        val intent: Intent?
                        if (user?.height == 0f && user.weight == 0f) {
                            intent = Intent(this@LoginActivity, ProfileEditActivity::class.java)
                            intent.putExtra(Constant.TYPE_PROFILE, 0)
                            intent.putExtra(Constant.USER_ID, PreferencesUtil.idUser)
                        } else if (PreferencesUtil.isAgreedTerms) {
                            intent = Intent(this@LoginActivity, MainActivity::class.java)
                            loadingDialog?.dismissDialog()

                        } else {
                            intent = Intent(this@LoginActivity, TutorialActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        loadingDialog?.dismissDialog()
                        Toast.makeText(baseContext, "Password Wrong.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(baseContext, "Login Fail", Toast.LENGTH_SHORT).show()
                loadingDialog?.dismissDialog()
            }
    }
}
