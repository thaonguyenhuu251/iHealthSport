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
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityLoginBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.view.component.LoadingDialog
import com.htnguyen.ihealth.view.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override val layout: Int
        get() = R.layout.activity_login

    override val viewModel: LoginViewModel by viewModel()

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    private lateinit var auth: FirebaseAuth
    private var loadingDialog: LoadingDialog? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindHideKeyboardListener(binding.root, binding.content)

        auth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog(this)
        db = Firebase.firestore
        binding.lnNotAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        actionMoveScreen()
    }

    private fun actionMoveScreen() {
        binding.tvSignIn.setOnClickListener {
            loadingDialog?.showDialog()
            if (viewModel.password.value.toString().contains("@")) {
                loginWithEmail()
            } else {
                loginWithPhoneNumber()
            }
        }
    }

    private fun loginWithEmail() {
        val email = viewModel.idAccount.value.toString()
        val password = viewModel.password.value.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    loadingDialog?.dismissDialog()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    loadingDialog?.dismissDialog()
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginWithPhoneNumber() {
        val phone = viewModel.idAccount.value.toString()
        val password = viewModel.password.value.toString()

        db.collection("user").document(phone).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    if (user?.passWord == password) {
                        val login = Intent(this@LoginActivity, MainActivity::class.java)
                        loadingDialog?.dismissDialog()
                        startActivity(login)
                        finish()
                    } else {

                        loadingDialog?.dismissDialog()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(baseContext, "Password Wrong.",
                    Toast.LENGTH_SHORT).show()
                loadingDialog?.dismissDialog()
            }
    }

}
