package com.example.planflow.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.planflow.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PlanFlowNotificationManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private const val REMINDERS_CHANNEL_ID = "reminders"

        private const val  EXPENSE_REMINDER_NOTIFICATION_ID = 1001
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notifyExpenseReminder() {
        notifyReminderNotification(
            title = context.getString(R.string.notification_expense_reminder_title),
            content = context.getString(R.string.notification_expense_reminder_content),
            notificationID = EXPENSE_REMINDER_NOTIFICATION_ID
        )
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun notifyReminderNotification(title: String, content: String, notificationID: Int) {
        createRemindersNotificationChannel()
        val notification = NotificationCompat.Builder(context, REMINDERS_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context)
            .notify(notificationID, notification)
    }

    private fun createRemindersNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDERS_CHANNEL_ID,
                context.getString(R.string.notification_channel_reminders_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

}
