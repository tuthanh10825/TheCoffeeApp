package com.example.thecoffeeapp.ui.component

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

import com.example.thecoffeeapp.R

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MyChannel"
        val descriptionText = "This is my notification channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("my_channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


fun sendNotification(context: Context, title: String,
                     message: String,
                     pendingIntent: PendingIntent? = null
) {
    val builder = NotificationCompat.Builder(context, "my_channel_id")
        .setSmallIcon(R.drawable.ic_logo)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    if (pendingIntent != null) {
        builder.setContentIntent(pendingIntent)
    }

    // notificationId is a unique int for each notification that you must define.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Notification permission not granted")
            return
        }
    }
    val uniqueId = System.currentTimeMillis().toInt()

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(uniqueId, builder.build())
}