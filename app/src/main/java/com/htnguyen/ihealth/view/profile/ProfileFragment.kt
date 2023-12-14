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
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.TutorialActivity
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentProfileBinding
import com.htnguyen.ihealth.model.ActivityDaily
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.SimpleDateFormat
import com.htnguyen.ihealth.support.endYear
import com.htnguyen.ihealth.support.firstYear
import com.htnguyen.ihealth.util.*
import com.htnguyen.ihealth.util.CommonUtils.getCaloriesInt
import com.htnguyen.ihealth.util.CommonUtils.getDistanceInt
import com.htnguyen.ihealth.util.Constant.getRealPathFromUri
import com.htnguyen.ihealth.view.component.LoadingDialog2
import java.io.File
import java.io.IOException

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_profile

    private val viewModel by viewModels<ProfileViewModel>()

    private val storageRef = Firebase.storage.reference

    private var loadingDialog: LoadingDialog2? = null
    var totalStep = 0

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

        for (i in Calendar().firstYear .. Calendar().endYear step 86400000) {
            getStepDay(i)
        }
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

        viewModel.birthDay.value = SimpleDateFormat("dd/MM/yyyy")
            .format(PreferencesUtil.userBirthDay)
            .toString()
    }

    private fun getStepDay(day: Long) {
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily")
            .child("date$day")
            .get().addOnSuccessListener { data ->
                val activityDaily = data.getValue<ActivityDaily>()
                if (activityDaily != null) {
                    totalStep += activityDaily.step ?: 0
                }
            }.addOnCompleteListener {
                viewModel.achieveStep.value = getString(R.string.profile_achieve_step, totalStep)
                viewModel.achieveCalo.value = getString(R.string.profile_achieve_calo, getCaloriesInt(totalStep))
                viewModel.achieveDistance.value = getString(R.string.profile_achieve_distance, getDistanceInt(totalStep))
                viewModel.achieveMoutain.value = getString(R.string.profile_achieve_mountain, getCaloriesInt(totalStep))
                viewModel.achieveTime.value = getString(R.string.profile_achieve_hour, (totalStep/4000))
            }
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