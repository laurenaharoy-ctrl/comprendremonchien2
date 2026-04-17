package com.laurena.comprendremonchien

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.laurena.comprendremonchien.R

class RappelWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val nomChien = inputData.getString("nom_chien") ?: "votre chien"
        val channelId = "rappel_bilan"
        val manager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Rappels bilan",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Rappels pour refaire le bilan de votre chien"
        }
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Et si vous refaisiez le bilan ?")
            .setContentText("Beaucoup de choses peuvent avoir évolué pour $nomChien 🐾")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
        return Result.success()
    }
}