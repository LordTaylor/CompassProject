package com.acante.compassproject

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.acante.compassproject.ui.compass_ui.CompassFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        supportFragmentManager.beginTransaction().replace(R.id.first_container,CompassFragment()).commit()
    }

    public override fun onResume() {
        super.onResume()
        // This registers mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                mMessageReceiver,
                IntentFilter("my-integer")
            )
    }

    // Handling the received Intents for the "my-integer" event
    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Extract data included in the Intent
            val yourInteger = intent.getIntExtra("message", -1/*default value*/)
        }
    }

    override fun onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mMessageReceiver)
        super.onPause()
    }
}
