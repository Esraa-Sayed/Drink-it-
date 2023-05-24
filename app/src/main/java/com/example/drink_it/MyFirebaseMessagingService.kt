package com.example.drink_it

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var broadcaster: LocalBroadcastManager? = null
    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        handleMessage(message)
    }

    private fun handleMessage(message: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())

        handler.post(kotlinx.coroutines.Runnable {
            message.notification.let {
                val intent = Intent("MyData")
                intent.putExtra("message", message.data["text"])
                broadcaster?.sendBroadcast(intent)
            }
            Log.e("TAG", "handleMessage: ${getString(R.string.handle_notification_now)}")
        }
        )

    }

}