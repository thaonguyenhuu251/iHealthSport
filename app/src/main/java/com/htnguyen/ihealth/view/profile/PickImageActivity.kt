package com.htnguyen.ihealth.view.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealth.adapter.GalleryPicturesAdapter
import com.htnguyen.ihealth.base.SpaceItemDecoration
import com.htnguyen.ihealth.databinding.ActivityPickImageBinding
import com.htnguyen.ihealth.model.GalleryPicture
import com.htnguyen.ihealth.util.Constant

class PickImageActivity : AppCompatActivity() {
    private val adapter by lazy {
        GalleryPicturesAdapter(pictures, 10, this)
    }

    private val galleryViewModel: GalleryViewModel by viewModels()

    private val pictures by lazy {
        ArrayList<GalleryPicture>(galleryViewModel.getGallerySize(this))
    }

    private lateinit var binding: ActivityPickImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestReadStoragePermission()
    }

    private fun requestReadStoragePermission() {
        val readStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, readStorage) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(readStorage), 3)
        } else init()
    }

    private fun init() {
        val layoutManager = GridLayoutManager(this, 3)
        val pageSize = 20

        binding.rcvImages.layoutManager = layoutManager
        binding.rcvImages.addItemDecoration(SpaceItemDecoration(1))
        binding.rcvImages.adapter = adapter

        adapter.setOnClickListener { galleryPicture ->
            val resultIntent = Intent()
            resultIntent.putExtra(Constant.KEY_PATH_IMAGE, galleryPicture.path)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.rcvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastVisibleItemPosition() == pictures.lastIndex) {
                    loadPictures(pageSize)
                }
            }
        })

        binding.imgBack.setOnClickListener {
            finish()
        }

        loadPictures(pageSize)
    }


    private fun loadPictures(pageSize: Int) {
        galleryViewModel.getImagesFromGallery(this, pageSize) {
            if (it.isNotEmpty()) {
                pictures.addAll(it)
                adapter.notifyItemRangeInserted(pictures.size, it.size)
            }
        }
    }

    private fun showToast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            init()
        else {
            showToast("Permission Required to Fetch Gallery.")
            super.onBackPressed()
        }
    }

}