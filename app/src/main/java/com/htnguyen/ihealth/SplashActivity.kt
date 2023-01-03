package com.htnguyen.ihealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog2
import com.htnguyen.ihealth.view.login.LoginActivity
import com.htnguyen.ihealth.view.main.MainActivity
import com.htnguyen.ihealth.view.profile.ProfileViewModel

class SplashActivity : AppCompatActivity() {

    private var loadingDialog: LoadingDialog2? = null
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog2(this)
        if(!PreferencesUtil.idUser.isNullOrEmpty() && !PreferencesUtil.passWord.isNullOrEmpty()) {
            loadingDialog?.showDialog()
            if (PreferencesUtil.idUser.toString().contains("@")) {
                loginWithEmail()
            } else {
                loginWithPhoneNumber()
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }, 1000)
        }
    }

    private fun loginWithEmail() {
        auth.signInWithEmailAndPassword(PreferencesUtil.idUser.toString(), PreferencesUtil.passWord.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    getProfileUser()
                } else {
                    // If sign in fails, display a message to the user.
                    loadingDialog?.dismissDialog()
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginWithPhoneNumber() {
        db.collection("user").document(PreferencesUtil.idUser!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    if (user?.passWord == PreferencesUtil.passWord) {
                        if (user != null) {
                            PreferencesUtil.userName = user.name
                            PreferencesUtil.userBirthDay = user.birthDay
                            PreferencesUtil.userGender = user.gender ?: false
                            PreferencesUtil.userHeight = user.height
                            PreferencesUtil.userWeight = user.weight
                        }
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    } else {
                        loadingDialog?.dismissDialog()
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
            .addOnFailureListener { exception ->
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
                Toast.makeText(baseContext, "Password Wrong.",
                    Toast.LENGTH_SHORT).show()
                loadingDialog?.dismissDialog()
            }
    }

    private fun getProfileUser() {
        db.collection("user").document(PreferencesUtil.idUser!!).get()
            .addOnSuccessListener { result ->
                val user = result.toObject(User::class.java)
                if (user != null) {
                    PreferencesUtil.userName = user.name
                    PreferencesUtil.userBirthDay = user.birthDay
                    PreferencesUtil.userGender = user.gender ?: false
                    PreferencesUtil.userHeight = user.height
                    PreferencesUtil.userWeight = user.weight
                }
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                loadingDialog?.dismissDialog()
            }
    }
}