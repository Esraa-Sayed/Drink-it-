package com.example.drink_it

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var textView:TextView
    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            textView.text = intent.extras?.getString("message")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view_notification)
        findViewById<Button>(R.id.button_retrieve_token).setOnClickListener {
            if(checkGooglePlaySevices()){
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    // 3
                    val token = task.result

                    // 4
                    val msg = getString(R.string.token_prefix, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                })
            }else {
                //You won't be able to send notifications to this device
                Log.w(TAG, "Device doesn't have google play services")
            }
            val bundle = intent.extras
            if(bundle != null){
                textView.text = bundle.getString("text")
            }
        }
    }
    override fun onStart() {
        super.onStart()

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("MyData"))
    }

    override fun onStop() {
        super.onStop()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }
    private fun checkGooglePlaySevices(): Boolean {
       val status = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this)
        return if(status != ConnectionResult.SUCCESS){
            Log.e(TAG,"Error")
            false
        }else{
            Log.i(TAG, "Google play services updated")
            true
        }
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}