package com.htnguyen.ihealth.helper

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.util.CommonUtils.getCalories
import com.htnguyen.ihealth.util.CommonUtils.getDistanceCovered
import com.htnguyen.ihealth.util.PreferencesUtil
import com.htnguyen.ihealth.view.main.MainActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GeneralHelper {
    companion object{
        fun getToadyDate(): String{
            val date: Date = Calendar.getInstance().time
            val df: DateFormat = SimpleDateFormat("dd MM yyyy")
            return df.format(date)
        }

        fun updateNotification(context: Context, service: Service, step: Int){
            val NOTIFICATION_ID = 7837
            var notiBuilder: Notification.Builder = Notification.Builder(context)
            var notiManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            var notification = NotificationCompat.Builder(context, "CHANNEL_ID")
                .setContentTitle("Step Counter")
                .setContentText(step.toString())
                .setTicker(step.toString())
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Step Counter"))
                .setStyle(NotificationCompat.BigTextStyle().bigText(step.toString() + " Steps / " + getCalories(step) + getDistanceCovered(step)))
                .setSmallIcon(R.mipmap.ic_notification)
                .setColor(context.getColor(R.color.color_blue_general))
                .setContentIntent(pendingIntent)
                .setProgress(PreferencesUtil.followStep, step, false)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build()

            service.startForeground(NOTIFICATION_ID, notification)
            notiManager.notify(NOTIFICATION_ID, notification)

        }
    }
}