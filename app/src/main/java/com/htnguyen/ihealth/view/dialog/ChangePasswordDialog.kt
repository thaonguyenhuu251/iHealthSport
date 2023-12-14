package com.htnguyen.ihealth.view.dialog

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.TutorialActivity
import com.htnguyen.ihealth.base.BaseDialog
import com.htnguyen.ihealth.databinding.DialogChangePasswordBinding
import com.htnguyen.ihealth.model.User
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog2
import com.htnguyen.ihealth.view.main.MainActivity
import com.htnguyen.ihealth.view.profile.ChangePasswordViewModel
import com.htnguyen.ihealth.view.profile.ProfileEditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordDialog :
    BaseDialog.ScaffoldBinding<DialogChangePasswordBinding, ChangePasswordViewModel>() {
    override val layout: Int = R.layout.dialog_change_password
    override val style: Int = R.style.MyDialog
    override val height: Int = WindowManager.LayoutParams.WRAP_CONTENT
    override val width: Int = WindowManager.LayoutParams.WRAP_CONTENT
    override val dim: Float? get() = 0.2f
    override val cancelable: Boolean = false

    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ChangePasswordViewModel by viewModel()
    private var loadingDialog: LoadingDialog2? = null

    override fun onCreateView(savedInstanceState: Bundle?) {
        super.onCreateView(savedInstanceState)
        initViewChangePassword()
        loadingDialog = LoadingDialog2(requireActivity())

    }

    private fun initViewChangePassword() {
        binding.edtConfirmPassword.doAfterTextChanged {
            viewModel.isValidatePasswordMatch()
        }

        binding.edtConfirmPassword.doAfterTextChanged {
            viewModel.isValidatePasswordMatch()
        }
        binding.txtOKLanguage.setOnClickListener {
            viewModel.isOldPasswordEmpty()
            if (viewModel.oldPassword.value == PreferencesUtil.passWord) {
                if (viewModel.isValidatePasswordFinish()) {
                    loadingDialog?.showDialog()
                    FirebaseUtils.db.collection("UserLogin").document(PreferencesUtil.account!!)
                        .update("password", viewModel.newPassword.value.toString())
                        .addOnSuccessListener {
                            loadingDialog?.dismissDialog()
                            PreferencesUtil.passWord = viewModel.newPassword.value.toString()
                            Toast.makeText(requireContext(), "Update password success", Toast.LENGTH_LONG).show()
                            dismiss()
                        }.addOnFailureListener {
                            loadingDialog?.dismissDialog()
                        }
                }
            } else {
                viewModel.validationOldPasswordMsg.value = "Password old wrong"
            }

        }
        binding.txtCancelLanguage.setOnClickListener {
            dismiss()
        }

    }

}