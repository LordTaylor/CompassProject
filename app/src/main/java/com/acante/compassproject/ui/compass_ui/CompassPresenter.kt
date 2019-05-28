package com.acante.compassproject.ui.compass_ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.acante.compassproject.ui.compass_ui.CompassContract.*
import com.acante.compassproject.ui.compass_ui.ui_elements.TargetArrow
import android.hardware.SensorManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


class CompassPresenter : Presenter, LocationListener, SensorEventListener {
    val TAG: String = "CompassPresenter"
    lateinit var view: View
    var context: Context
    var positionX: Double = 0.0
    var posittionY: Double = 0.0

    var targetX: Double = 0.0
    var targetY: Double = 0.0
    var targetArrow: TargetArrow


    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val R = FloatArray(9)
    private val I = FloatArray(9)

    private var azimuth: Float = 0.toFloat()
    private var azimuthFix: Float = 0.toFloat()

    var northDe: Float = 0f

    constructor(context: Context) {
        this.context = context
        targetArrow = TargetArrow(0.0, 0.0)

    }


    override fun onAtach(view: View) {
        this.view = view
    }

    override fun setTargetLongitude(longitude: Double) {
        targetY = longitude
        updateTarget(targetX, targetY)
    }

    override fun setTargetLatitude(latitude: Double) {
        targetX = latitude
        updateTarget(targetX, targetY)
    }

    override fun onLocationChanged(location: Location?) {
        positionX = location!!.latitude
        posittionY = location.longitude
        updateMyLocation(positionX, posittionY)
    }

    private fun updateTarget(latitude: Double, longitude: Double) {
        if (::view.isInitialized) {
            targetArrow.setTarget(latitude, longitude)
            view.displayTargetLocation(latitude, longitude)
        }
    }

    private fun updateMyLocation(latitude: Double, longitude: Double) {
        if (::view.isInitialized) {
            view.updateTarget(targetArrow.setMyLocation(latitude, longitude))
            view.displatyMyLocation(latitude, longitude)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

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
            view.updateNorth(azimuthFix)
        }
    }
}