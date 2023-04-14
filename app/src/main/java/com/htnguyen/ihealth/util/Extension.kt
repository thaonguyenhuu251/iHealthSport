package com.htnguyen.ihealth.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.InputFilter
import android.text.TextUtils
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("RestrictedApi")
fun Activity.showFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        val controller: WindowInsetsController? =
            window.insetsController
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        window.getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

fun Context.getFileFromUri(contentURI: Uri): File? {
    return try {
        val inputStream = contentResolver?.openInputStream(contentURI)
        val file = createTempFile()
        file.copyInputStreamToFile(inputStream!!)
        file
    } catch (e: java.lang.Exception) {
        null
    }
}

fun File.copyInputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
        this.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
}

/*fun Context.showAlertDialog(
    message: String,
    positiveLabel: String,
    negativeLabel: String,
    onPositiveClickListener: () -> Unit = {},
    onNegativeClickListener: () -> Unit = {},
) {
    val builder = AlertDialog.Builder(this, R.style.FinishActionDialog)

    val mLayoutInflater = LayoutInflater.from(this)
    val mView: View = mLayoutInflater.inflate(R.layout.dialog_alert, null)
    val mTextView = mView.findViewById(R.id.message) as TextView
    mTextView.text = message

    val positiveButton = mView.findViewById<Button>(R.id.btn_positive)
    positiveButton.text = positiveLabel

    val negativeButton = mView.findViewById<Button>(R.id.btn_negative)
    negativeButton.text = negativeLabel

    builder.setCustomTitle(mView)

    val dialog = builder.show()
    positiveButton.setOnClickListener {
        dialog.dismiss()
        onPositiveClickListener()
    }


    negativeButton.setOnClickListener {
        dialog.dismiss()
        onNegativeClickListener()
    }
}

fun Context.showAlertDialogError(
    onNegativeClickListener: () -> Unit = {},
) {
    val builder = AlertDialog.Builder(this, R.style.FinishActionDialog)

    val mLayoutInflater = LayoutInflater.from(this)
    val mView: View = mLayoutInflater.inflate(R.layout.dialog_error, null)

    val negativeButton = mView.findViewById<Button>(R.id.btn_negative)
    builder.setCustomTitle(mView)

    val dialog = builder.show()
    negativeButton.setOnClickListener {
        dialog.dismiss()
        onNegativeClickListener()
    }
}

fun Context.showAlertDialogRemind(context: Context, onNegativeClickListener: () -> Unit = {}) {

    val builder = AlertDialog.Builder(context, R.style.FinishActionDialog)

    val mLayoutInflater = LayoutInflater.from(context)
    val mView: View = mLayoutInflater.inflate(R.layout.dialog_action_on_tasks, null)

    val positiveButton = mView.findViewById<ImageButton>(R.id.img_close)
    val remindButton = mView.findViewById<AppCompatButton>(R.id.btn_remind)

    builder.setCustomTitle(mView)
    builder.setCancelable(false)

    val dialog = builder.show()
    positiveButton.setOnClickListener {
        dialog.dismiss()
    }
    remindButton.setOnClickListener {
        dialog.dismiss()
        onNegativeClickListener()
    }
}*/


/*fun Context.showDefaultPopup(
    title: String?,
    message: String,
    buttonLabel: String,
    onClickListener: () -> Unit = {},
) {
    this.showDefaultPopup(
        title,
        message,
        buttonLabel,
        onClickListener,
        true
    )
}*/

/*fun Context.showDefaultPopup(
    title: String?,
    message: String,
    buttonLabel: String,
    onClickListener: () -> Unit = {},
    isCancellable: Boolean,
) {
    val okButton: Button
    val builder = AlertDialog.Builder(this, R.style.FinishActionDialog).apply {
        val mLayoutInflater = LayoutInflater.from(context)
        val mView: View = mLayoutInflater.inflate(R.layout.dialog_one_button, null)
        val tvTitle: TextView = mView.findViewById(R.id.title)
        val tvMessage: TextView = mView.findViewById(R.id.message)
        okButton = mView.findViewById(R.id.ok_button)
        if (TextUtils.isEmpty(title)) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.text = title
        }
        tvMessage.text = message
        okButton.text = buttonLabel
        setCustomTitle(mView)
    }.setCancelable(isCancellable)
    if (title == getString(R.string.label_maintenance) || title == getString(R.string.no_internet_title) || message == getString(
            R.string.update_require_txt
        )
    ) {
        builder.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (message == getString(R.string.update_require_txt)) {
                    val appPackageName = packageName
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$appPackageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                            )
                        )
                    }
                } else {
                    val homeIntent = Intent(Intent.ACTION_MAIN)
                    homeIntent.addCategory(Intent.CATEGORY_HOME)
                    homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(homeIntent)
                    System.exit(1)
                }

                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

    val dialog = builder.show()

    okButton.setOnClickListener {
        dialog.dismiss()
        onClickListener()
    }
}

fun Context.showErrorDialog(message: String) {
    if (message == PoketoApplication.mInstance.getString(R.string.no_internet_message)) {
        this.showDefaultPopup(PoketoApplication.mInstance.getString(R.string.no_internet_title),
            message,
            getString(R.string.ok_button),
            onClickListener = {})

    } else {
        this.showDefaultPopup(null, message, getString(R.string.ok_button), onClickListener = {})
    }


}*/


fun Context.getDeviceId(): String {
    return Settings.Secure.getString(this.contentResolver,
        Settings.Secure.ANDROID_ID)
}

/*fun ImageView.loadImageUrl(url: String?) {
    url?.let {
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.ic_profile)
            .into(this)

    }
}*/

