package nokia.tablefootball.tablefootballandroid.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nokia.tablefootball.tablefootballandroid.R

open class MessagingHandlingService : FirebaseMessagingService() {

    val TAG : String = "FIREBASE"

    override fun onNewToken(token : String?){
//        super.onNewToken(token)
//        FirebaseInstanceId.getInstance().getToken()
//        Log.e("NEW TOKEN", token)

        Log.d(TAG,"Refreshed token: $token")
    }

//     override fun onMessageReceived(remoteMessage: RemoteMessage?){
//        super.onMessageReceived(remoteMessage)
//        Log.d(TAG, "From: ${remoteMessage?.from}")
//        Log.d(TAG, "Notification msg body: ${remoteMessage?.notification?.body}")
//    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "Nilesh_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications", NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.description = "Description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        notificationBuilder.setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(remoteMessage!!.getNotification()!!.getBody())
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)



        notificationManager.notify(1000, notificationBuilder.build())

    }
}