package com.asfoundation.wallet.support

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.asf.wallet.R
import dagger.android.AndroidInjection
import dagger.android.DaggerBroadcastReceiver
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AlarmManagerBroadcastReceiver : DaggerBroadcastReceiver(), HasAndroidInjector {

  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Any>

  @Inject
  lateinit var supportInteractor: SupportInteractor

  lateinit var notificationManager: NotificationManager

  companion object {
    const val NOTIFICATION_SERVICE_ID = 77794
    private const val CHANNEL_ID = "support_notification_channel_id"
    private const val CHANNEL_NAME = "Support notification channel"

    @JvmStatic
    fun scheduleAlarm(context: Context) {
      val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

      val intent = Intent(context, AlarmManagerBroadcastReceiver::class.java)

      val pendingIntent =
          PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

      val repeatInterval = TimeUnit.MINUTES.toMillis(15)
      val triggerTime: Long = SystemClock.elapsedRealtime() + repeatInterval
      alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,
          repeatInterval, pendingIntent)
    }

  }

  override fun androidInjector() = androidInjector

  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    AndroidInjection.inject(this, context)

    notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (supportInteractor.shouldShowNotification()) {
      supportInteractor.updateUnreadConversations()
      notificationManager.notify(NOTIFICATION_SERVICE_ID, createNotification(context).build())
    }
  }

  private fun createNotification(context: Context): NotificationCompat.Builder {
    val okPendingIntent = createNotificationClickIntent(context)
    val dismissPendingIntent = createNotificationDismissIntent(context)
    val builder: NotificationCompat.Builder
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
      builder = NotificationCompat.Builder(context, CHANNEL_ID)
      notificationManager.createNotificationChannel(notificationChannel)
    } else {
      builder = NotificationCompat.Builder(context, CHANNEL_ID)
    }
    return builder.setContentTitle(context.getString(R.string.support_new_message_title))
        .setAutoCancel(true)
        .setContentIntent(okPendingIntent)
        .addAction(0, context.getString(R.string.dismiss_button), dismissPendingIntent)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentText(context.getString(R.string.support_new_message_button))
  }

  private fun createNotificationClickIntent(context: Context): PendingIntent {
    val intent = SupportNotificationBroadcastReceiver.newIntent(context)
    intent.putExtra(SupportNotificationBroadcastReceiver.ACTION_KEY,
        SupportNotificationBroadcastReceiver.ACTION_CHECK_MESSAGES)
    return PendingIntent.getBroadcast(context, 0, intent, 0)
  }

  private fun createNotificationDismissIntent(context: Context): PendingIntent {
    val intent = SupportNotificationBroadcastReceiver.newIntent(context)
    intent.putExtra(SupportNotificationBroadcastReceiver.ACTION_KEY,
        SupportNotificationBroadcastReceiver.ACTION_DISMISS)
    return PendingIntent.getBroadcast(context, 1, intent, 0)
  }
}