package nokia.tablefootball.tablefootballandroid.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nokia.tablefootball.tablefootballandroid.R

//https://github.com/firebase/quickstart-android/blob/4a0ecd01c3c2523d1d119eb676405c56d1f6e7ef/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/kotlin/MyFirebaseMessagingService.kt
open class FirebaseNotificationService : FirebaseMessagingService()
{
    val TAG: String = "FIREBASE"

    override fun onNewToken(token: String?)
    {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?)
    {
        val channelId = "Channel id here"

        val defualtSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)

        notificationBuilder
            .setDefaults(Notification.DEFAULT_ALL)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(remoteMessage!!.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setSound(defualtSoundUri)
            .setSmallIcon(R.drawable.football)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //TODO handle >=Oreo notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(channelId,
                "Common notifications",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1000, notificationBuilder.build())

    }

}