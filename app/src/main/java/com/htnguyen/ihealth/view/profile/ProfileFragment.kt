package com.htnguyen.ihealth.view.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.TutorialActivity
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentProfileBinding
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.Constant.getRealPathFromUri
import com.htnguyen.ihealth.util.Event
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.component.LoadingDialog2
import java.io.File
import java.io.IOException

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_profile

    private val viewModel by viewModels<ProfileViewModel>()

    private val storageRef = Firebase.storage.reference

    private var loadingDialog: LoadingDialog2? = null

    @SuppressLint("ResourceAsColor")
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val data = intent?.getStringExtra(Constant.KEY_PATH_IMAGE)

                Glide.with(this).load(data).into(binding.imgAvatar)

                val filePath = getRealPathFromUri(requireContext(), Uri.parse(data))
                uploadFile(filePath!!)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadingDialog = LoadingDialog2(requireActivity())
        initView()
        actionMoveScreen()
    }

    private fun initView() {
        Glide.with(this).load(PreferencesUtil.userPhotoUrl)
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_profile_achievement))
            .into(binding.imgAvatar)
    }


    private fun actionMoveScreen() {
        binding.layoutProfileInformation.txtEdit.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileEditActivity::class.java)
            intent.putExtra(Constant.TYPE_PROFILE, 1)
            intent.putExtra(Constant.USER_ID, PreferencesUtil.idUser)
            requireActivity().startActivity(intent)
        }

        binding.imgCamera.setOnClickListener {
            (activity as BaseActivity<*, *>).goToGallery()
            startForResult.launch(Intent(requireActivity(), PickImageActivity::class.java))
        }

        binding.imgSetting.setOnClickListener {
            Event.eventOpenSetting()
        }

        binding.viewModel = viewModel

        viewModel.birthDay.value = SimpleDateFormat(getString(R.string.common_format_date))
            .format(PreferencesUtil.userBirthDay)
            .toString()
    }

    private fun uploadFile(fileName: String) {
        loadingDialog?.showDialog()
        val file = Uri.fromFile(File(fileName))
        val ref = storageRef.child("image_user/${System.currentTimeMillis()}")
        val uploadTask = ref.putFile(file)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {

                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                FirebaseUtils.db.collection("user")
                    .document(PreferencesUtil.idUser!!)
                    .update(
                        mapOf(
                            "photoUrl" to downloadUri.toString()
                        )
                    )
                    .addOnSuccessListener {
                        PreferencesUtil.userPhotoUrl = downloadUri.toString()
                        loadingDialog?.dismissDialog()
                    }
                    .addOnFailureListener { e ->
                        loadingDialog?.dismissDialog()
                    }

            } else {

            }
        }

        try {
            // function throw ra exception
        } catch (e: IOException) {

        }

    }
}