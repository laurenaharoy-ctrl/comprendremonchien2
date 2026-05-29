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
        val nomChien = inputData.getString("nom_chien") ?: nomChienAffiche("")
        val channelId = "rappel_bilan"
        val manager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            strNotifChannelNom(),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = strNotifChannelDesc()
        }
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(strNotifTitre())
            .setContentText(strNotifTexte(nomChien))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
        return Result.success()
    }
}