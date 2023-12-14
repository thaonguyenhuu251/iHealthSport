package com.htnguyen.ihealth.view.component

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnKeyListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.databinding.DialogOtpVerificationBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.model.UserLogin
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.profile.ProfileEditActivity
import kotlin.random.Random


class OTPVerificationDialog : DialogFragment() {
    private lateinit var binding: DialogOtpVerificationBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var resendEnable = false
    var selectedPosition = 0
    var numberPhone: String? = null
    var passWord: String? = null
    var otp: String? = null
    private var loadingDialog: LoadingDialog2? = null

    fun newInstance(numberPhone: String, passWord: String, otp: String): OTPVerificationDialog {
        val dialog = OTPVerificationDialog()
        val args = Bundle()
        args.putString(Constant.USER_PHONE, numberPhone)
        args.putString(Constant.USER_PASSWORD, passWord)
        args.putString(Constant.USER_OTP, otp)
        dialog.arguments = args
        return dialog
    }

    private val keyListener = object : OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                when (selectedPosition) {
                    5 -> {
                        selectedPosition = 4
                        showKeyboard(binding.edtOTP4)
                    }

                    4 -> {
                        selectedPosition = 3
                        showKeyboard(binding.edtOTP3)
                    }

                    3 -> {
                        selectedPosition = 2
                        showKeyboard(binding.edtOTP2)
                    }

                    2 -> {
                        selectedPosition = 1
                        showKeyboard(binding.edtOTP1)
                    }

                    1 -> {
                        selectedPosition = 0
                        showKeyboard(binding.edtOTP1)
                    }
                }

                binding.btnVerify.setBackgroundColor(Color.parseColor("#999999"))
                return true
            }

            return onKey(v, keyCode, event)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOtpVerificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialog = Dialog(requireContext())
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        setStyle(STYLE_NORMAL, R.style.MyDialog)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog2(requireActivity())
        numberPhone = arguments?.getString(Constant.USER_PHONE)
        passWord = arguments?.getString(Constant.USER_PASSWORD)
        otp = arguments?.getString(Constant.USER_OTP)

        initView()
    }

    private fun initView() {
        binding.btnVerify.setBackgroundColor(Color.parseColor("#999999"))
        binding.btnVerify.isEnabled = false
        binding.edtOTP1.addTextChangedListener(textWatcher)
        binding.edtOTP2.addTextChangedListener(textWatcher)
        binding.edtOTP3.addTextChangedListener(textWatcher)
        binding.edtOTP4.addTextChangedListener(textWatcher)
        binding.edtOTP5.addTextChangedListener(textWatcher)
        binding.edtOTP6.addTextChangedListener(textWatcher)

        showKeyboard(binding.edtOTP1)
        startCountDownTimer()
        binding.tvRequestCode.setOnClickListener {
            if (resendEnable) {
                startCountDownTimer()
            }
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.btnVerify.setOnClickListener {
            val getOTP = binding.edtOTP1.text.toString() +
                    binding.edtOTP2.text.toString() +
                    binding.edtOTP3.text.toString() +
                    binding.edtOTP4.text.toString() +
                    binding.edtOTP5.text.toString() +
                    binding.edtOTP6.text.toString()

            if (getOTP.trim().length == 6) {
                loadingDialog?.showDialog()
                val credential = PhoneAuthProvider.getCredential(otp!!, getOTP)
                signInWithPhoneAuthCredential(credential)
            }
        }

        keyListener
    }

    private val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {

        }

        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) {
                when (selectedPosition) {
                    0 -> {
                        selectedPosition = 1
                        showKeyboard(binding.edtOTP2)
                    }

                    1 -> {
                        selectedPosition = 2
                        showKeyboard(binding.edtOTP3)
                    }

                    2 -> {
                        selectedPosition = 3
                        showKeyboard(binding.edtOTP4)
                    }

                    3 -> {
                        selectedPosition = 4
                        showKeyboard(binding.edtOTP5)
                    }

                    4 -> {
                        selectedPosition = 5
                        showKeyboard(binding.edtOTP6)
                    }
                    else -> {
                        binding.btnVerify.setBackgroundColor(Color.parseColor("#E5463B"))
                        binding.btnVerify.isEnabled = true
                    }
                }
            }
        }
    }

    private fun showKeyboard(edtOTP: EditText) {
        edtOTP.requestFocus()
        val inputMethodManager: InputMethodManager =
            this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(edtOTP, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun startCountDownTimer() {
        resendEnable = false
        binding.btnVerify.setTextColor(Color.parseColor("#99000000"))

        val timer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvRequestCode.text = "Request Code (${millisUntilFinished / 1000})"
            }

            override fun onFinish() {
                binding.tvRequestCode.text = "Request Code"
                resendEnable = true
            }
        }
        timer.start()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val idUser = task.result?.user?.uid
                        ?: "user" + Calendar().timeInMillis.toString() + Random.nextInt(0, 999)
                            .toString()
                    loadingDialog?.dismissDialog()
                    addUser(idUser)

                } else {
                    loadingDialog?.dismissDialog()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }

                }
            }
    }


    private fun addUser(idUser: String) {
        val userNew = UserLogin(
            idUser = idUser,
            account = numberPhone,
            password = passWord
        )

        numberPhone?.let {
            db.collection("UserLogin").document(it).set(userNew)
                .addOnSuccessListener {
                    loadingDialog?.dismissDialog()

                    PreferencesUtil.idUser = idUser
                    PreferencesUtil.account = numberPhone
                    PreferencesUtil.passWord = passWord

                    val intent = Intent(requireActivity(), ProfileEditActivity::class.java)
                    intent.putExtra(Constant.TYPE_PROFILE, 0)
                    intent.putExtra(Constant.USER_ID, idUser)
                    intent.putExtra(Constant.USER_ACCOUNT, numberPhone)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .addOnFailureListener { e ->

                }
        }
    }

    companion object {
        const val TAG = "OTPDialog"
    }

}