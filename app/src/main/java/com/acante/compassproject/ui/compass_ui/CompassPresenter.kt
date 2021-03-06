package com.acante.compassproject.ui.compass_ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.acante.compassproject.ui.compass_ui.CompassContract.*
import com.acante.compassproject.ui.compass_ui.ui_elements.TargetArrow
import android.hardware.SensorManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation


class CompassPresenter : Presenter, LocationListener, SensorEventListener {
    val TAG: String = "CompassPresenter"
    private lateinit var view: View
    private var positionX: Double = 0.0
    private var posittionY: Double = 0.0

    private var targetX: Double = 0.0
    private var targetY: Double = 0.0
    private var targetArrow: TargetArrow = TargetArrow(55.0, 22.0)

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private val R = FloatArray(9)
    private val I = FloatArray(9)

    private var azimuth: Float = 0.toFloat()
    private var azimuthFix: Float = 0.toFloat()

    override fun onAttach(view: View) {
        this.view = view
    }

    override fun setTargetLongitude(longitude: String) {
        targetY = getDouble(longitude)
        updateTarget()
    }

    override fun setTargetLatitude(latitude: String) {
        targetX = getDouble(latitude)
        updateTarget()
    }

    override fun onLocationChanged(location: Location?) {
        positionX = location!!.latitude
        posittionY = location.longitude
        updateMyLocation()
    }

    private fun updateTarget() {
        targetArrow.setTarget(targetX, targetY)
        if (::view.isInitialized) {
            view.displayTargetLocation(targetX, targetY)
        }

    }

    private fun updateMyLocation() {
        targetArrow.setMyPosition(positionX, posittionY)
        if (::view.isInitialized) {
            view.displayMyLocation(positionX, posittionY)
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
        if (::view.isInitialized) {
            val alpha = 0.97f
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
                var animation = RotateAnimation(
                    azimuthFix,
                    azimuth, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                animation.fillAfter = true
                animation.duration = 210
                view.updateNorth(animation)
                animation = RotateAnimation(
                    (azimuthFix + targetArrow.targetAngle).toFloat(),
                    -(targetArrow.targetAngle + azimuth).toFloat(), Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                animation.fillAfter = true
                animation.duration = 210
                view.updateTarget(animation)

            }
        }
    }

    private fun getDouble(string: String): Double {
        return (string.toDoubleOrNull()) ?: return 0.0
    }
}