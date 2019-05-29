package com.acante.compassproject.location_utils

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.acante.compassproject.MainActivity


class SensorService : IntentService() ,SensorEventListener{
    override fun onHandleIntent(intent: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val R = FloatArray(9)
    private val I = FloatArray(9)

    private var azimuth: Float = 0.toFloat()
    private var azimuthFix: Float = 0.toFloat()

    override fun onSensorChanged(event: SensorEvent?) {

        var alpha = 0.97f
        if (event!!.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
            mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
            mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
            mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
            mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
        }
        val success = SensorManager.getRotationMatrix(
            R, I, mGravity,
            mGeomagnetic
        )
        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R, orientation)
            azimuth = (orientation[0].toDouble() * 180 / Math.PI).toFloat()
            azimuthFix = -azimuth

        }

    }

    private fun sendMessage() {
        // The string "my-integer" will be used to filer the intent
        val intent = Intent("my-integer")
        // Adding some data
        intent.putExtra("message", azimuthFix)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        setUpSensors()

    }

    private fun setUpSensors() {
        var sensorManager: SensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            10000, 10000
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            10000, 10000
        )
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
