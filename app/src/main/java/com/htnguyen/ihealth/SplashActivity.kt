package com.htnguyen.ihealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.model.UserLogin
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog2
import com.htnguyen.ihealth.view.login.LoginActivity
import com.htnguyen.ihealth.view.main.MainActivity
import com.htnguyen.ihealth.view.profile.ProfileEditActivity

class SplashActivity : AppCompatActivity() {

    private var loadingDialog: LoadingDialog2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loadingDialog = LoadingDialog2(this)

        if(!PreferencesUtil.idUser.isNullOrEmpty() && !PreferencesUtil.passWord.isNullOrEmpty()) {
            loadingDialog?.showDialog()
            loginWithAccountAndPassword()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }, 1000)
        }
    }

    private fun loginWithAccountAndPassword() {
        FirebaseUtils.db.collection("UserLogin").document(PreferencesUtil.account!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(UserLogin::class.java)
                    if (user?.password == PreferencesUtil.passWord) {
                        getInformationUser()
                    } else {
                        loadingDialog?.dismissDialog()
                        Toast.makeText(baseContext, "Password Wrong", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
            .addOnFailureListener { exception ->
                loadingDialog?.dismissDialog()
                Toast.makeText(baseContext, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getInformationUser() {
        FirebaseUtils.db.collection("User").document(PreferencesUtil.idUser!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    loadingDialog?.dismissDialog()
                    val intent: Intent?
                    if (user?.height == 0f && user.weight == 0f) {
                        intent = Intent(this@SplashActivity, ProfileEditActivity::class.java)
                        intent.putExtra(Constant.TYPE_PROFILE, 0)
                        intent.putExtra(Constant.USER_ID, PreferencesUtil.idUser)
                    } else if (PreferencesUtil.isAgreedTerms) {
                        intent = Intent(this@SplashActivity, MainActivity::class.java)
                    } else {
                        intent = Intent(this@SplashActivity, TutorialActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                loadingDialog?.dismissDialog()
                Toast.makeText(baseContext, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

}