package com.htnguyen.ihealth.view.home

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.htnguyen.ihealth.support.OutputAnalyzer
import android.annotation.SuppressLint
import android.os.Looper
import com.htnguyen.ihealth.R
import android.widget.TextView
import android.widget.EditText
import com.htnguyen.ihealth.support.CameraService
import android.content.pm.PackageManager
import com.google.android.material.snackbar.Snackbar
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.Html
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import com.htnguyen.ihealth.model.EatAndDrink
import com.htnguyen.ihealth.model.HealthDaily
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.support.dateInMillis
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import java.text.SimpleDateFormat
import java.util.*

class MeasureHeartBeatActivity : AppCompatActivity(), OnRequestPermissionsResultCallback {
    private var analyzer: OutputAnalyzer? = null
    private val REQUEST_CODE_CAMERA = 0

    enum class VIEW_STATE {
        MEASUREMENT, SHOW_RESULTS
    }

    private var justShared = false

    private val mainHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MESSAGE_UPDATE_REALTIME) {
                (findViewById<View>(R.id.textView) as TextView).text = msg.obj.toString()
            }
            if (msg.what == MESSAGE_UPDATE_FINAL) {
                (findViewById<View>(R.id.editText) as EditText).setText(msg.obj.toString())

                // make sure menu items are enabled when it opens.
                val appMenu = (findViewById<View>(R.id.toolbar) as Toolbar).menu
                setViewState(VIEW_STATE.SHOW_RESULTS)
            }
            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString())
                (findViewById<View>(R.id.textView) as TextView).setText(
                    R.string.camera_not_found
                )
                analyzer!!.stop()
            }
        }
    }
    private val cameraService = CameraService(this, mainHandler)
    override fun onResume() {
        super.onResume()
        /*analyzer = OutputAnalyzer(this@MeasureHeartBeatActivity, findViewById(R.id.graphTextureView), mainHandler)
        val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
        val previewSurfaceTexture = cameraTextureView.surfaceTexture

        // justShared is set if one clicks the share button.
        if (previewSurfaceTexture != null && !justShared) {
            // this first appears when we close the application and switch back
            // - TextureView isn't quite ready at the first onResume.
            val previewSurface = Surface(previewSurfaceTexture)

            // show warning when there is no flash
            if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                    findViewById(R.id.constraintLayout),
                    getString(R.string.noFlashWarning),
                    Snackbar.LENGTH_LONG
                ).show()
            }

            // hide the new measurement item while another one is in progress in order to wait
            // for the previous one to finish

            cameraService.start(previewSurface)
            analyzer!!.measurePulse(cameraTextureView, cameraService)
        }*/
    }

    override fun onPause() {
        super.onPause()
        cameraService.stop()
        if (analyzer != null) analyzer!!.stop()
        analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure_heart_beat)
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                    findViewById(R.id.constraintLayout),
                    getString(R.string.cameraPermissionRequired),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        Log.i("MENU", "menu is being prepared")
        val inflater = menuInflater
        return super.onPrepareOptionsMenu(menu)
    }

    fun setViewState(state: VIEW_STATE?) {
        /*val appMenu = (findViewById<View>(R.id.toolbar) as Toolbar).menu
        if (state == VIEW_STATE.MEASUREMENT) {
            appMenu.getItem(MENU_INDEX_NEW_MEASUREMENT).isVisible = false
            appMenu.getItem(MENU_INDEX_EXPORT_RESULT).isVisible = false
            appMenu.getItem(MENU_INDEX_EXPORT_DETAILS).isVisible = false
            findViewById<View>(R.id.floatingActionButton).visibility =
                View.INVISIBLE
        }
        else if (state == VIEW_STATE.SHOW_RESULTS) {
            findViewById<View>(R.id.floatingActionButton).visibility = View.VISIBLE
            appMenu.getItem(MENU_INDEX_EXPORT_RESULT).isVisible = true
            appMenu.getItem(MENU_INDEX_EXPORT_DETAILS).isVisible = true
            appMenu.getItem(MENU_INDEX_NEW_MEASUREMENT).isVisible = true
        }*/
    }

    fun onClickNewMeasurement(item: MenuItem?) {
        onClickNewMeasurement()
    }

    fun onClickNewMeasurement(view: View?) {
        onClickNewMeasurement()
    }

    fun onClickNewMeasurement() {
        analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler)

        // clear prior results
        val empty = CharArray(0)
        (findViewById<View>(R.id.editText) as EditText).setText(empty, 0, 0)
        (findViewById<View>(R.id.textView) as TextView).setText(empty, 0, 0)

        // hide the new measurement item while another one is in progress in order to wait
        // for the previous one to finish
        // Exporting results cannot be done, either, as it would read from the already cleared UI.
        setViewState(VIEW_STATE.MEASUREMENT)
        val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
        val previewSurfaceTexture = cameraTextureView.surfaceTexture
        if (previewSurfaceTexture != null) {
            // this first appears when we close the application and switch back
            // - TextureView isn't quite ready at the first onResume.
            val previewSurface = Surface(previewSurfaceTexture)
            cameraService.start(previewSurface)
            analyzer!!.measurePulse(cameraTextureView, cameraService)
        }
    }

    fun onClickExportResult(item: MenuItem?) {
        val intent = getTextIntent((findViewById<View>(R.id.textView) as TextView).text as String)
        justShared = true
        startActivity(Intent.createChooser(intent, getString(R.string.send_output_to)))
    }

    fun onClickExportDetails(item: MenuItem?) {
        val intent = getTextIntent((findViewById<View>(R.id.editText) as EditText).text.toString())
        justShared = true
        startActivity(Intent.createChooser(intent, getString(R.string.send_output_to)))
    }

    private fun getTextIntent(intentText: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_SUBJECT, String.format(
                getString(R.string.output_header_template),
                SimpleDateFormat(
                    getString(R.string.dateFormat),
                    Locale.getDefault()
                ).format(Date())
            )
        )
        intent.putExtra(Intent.EXTRA_TEXT, intentText)
        return intent
    }

    companion object {
        const val MESSAGE_UPDATE_REALTIME = 1
        const val MESSAGE_UPDATE_FINAL = 2
        const val MESSAGE_CAMERA_NOT_AVAILABLE = 3
        private const val MENU_INDEX_NEW_MEASUREMENT = 0
        private const val MENU_INDEX_EXPORT_RESULT = 1
        private const val MENU_INDEX_EXPORT_DETAILS = 2
    }
}