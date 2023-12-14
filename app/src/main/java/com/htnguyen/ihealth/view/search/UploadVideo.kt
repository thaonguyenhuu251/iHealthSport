package com.htnguyen.ihealth.view.search

import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask
import android.os.Bundle
import com.htnguyen.ihealth.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.webkit.MimeTypeMap
import java.util.Locale
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.htnguyen.ihealth.view.main.MainActivity

class UploadVideo private constructor() : AppCompatActivity() {
    var videoView: VideoView? = null
    var button: Button? = null
    var progressBar: ProgressBar? = null
    var editText: EditText? = null
    private var videoUri: Uri? = null
    var mediaController: MediaController? = null
    var storageReference: StorageReference? = null
    var databaseReference: DatabaseReference? = null
    var model: VideoModel? = null
    var uploadTask: UploadTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadvideo)
        model = VideoModel()
        storageReference = FirebaseStorage.getInstance().getReference("Video")
        databaseReference = FirebaseDatabase.getInstance().getReference("video")
        videoView = findViewById(R.id.videoview_main)
        button = findViewById(R.id.button_upload_main)
        progressBar = findViewById(R.id.progressBar_main)
        editText = findViewById(R.id.et_video_name)
        mediaController = MediaController(this)
        videoView?.setMediaController(mediaController)
        videoView?.start()
        button?.setOnClickListener(View.OnClickListener { UploadVideo() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO || resultCode == RESULT_OK || data != null) {
            videoUri = data!!.data
            videoView!!.setVideoURI(videoUri)
        }
    }

    fun ChooseVideo(view: View?) {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_VIDEO)
    }

    private fun getExt(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun ShowVideo(view: View?) {
        val intent = Intent(this@UploadVideo, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val PICK_VIDEO = 1
    }

    init {
        val videoName = editText!!.text.toString()
        val search = editText!!.text.toString().lowercase(Locale.getDefault())
        if (videoUri != null || !TextUtils.isEmpty(videoName)) {
            progressBar!!.visibility = View.VISIBLE
            val reference = storageReference!!.child(
                System.currentTimeMillis().toString() + "." + getExt(videoUri)
            )
            uploadTask = reference.putFile(videoUri!!)
            val urltask = uploadTask!!.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                reference.downloadUrl
            }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        progressBar!!.visibility = View.INVISIBLE
                        Toast.makeText(this@UploadVideo, "Data saved", Toast.LENGTH_SHORT).show()
                        model!!.name = videoName
                        model!!.videourl = downloadUrl.toString()
                        model!!.search = search
                        val i = databaseReference!!.push().key
                        databaseReference!!.child(i!!).setValue(model)
                    } else {
                        Toast.makeText(this@UploadVideo, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show()
        }
    }
}