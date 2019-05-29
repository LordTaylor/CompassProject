package com.acante.compassproject.ui.compass_ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.acante.compassproject.R
import kotlinx.android.synthetic.main.compass_fragment.*


class CompassFragment : Fragment(), CompassContract.View {
    val TAG = "CompassFragment"

    lateinit var presenter: CompassPresenter
    lateinit var sensorManager: SensorManager
    lateinit var locationManager: LocationManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.compass_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        setUpSensors()

    }

    override fun onPause() {
        super.onPause()
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(presenter)
    }

    private fun setUpSensors() {
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            presenter,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            10000, 10000
        )
        sensorManager.registerListener(
            presenter,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            10000, 10000
        )
        if (ContextCompat.checkSelfPermission(
                activity!!, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10f, presenter)
        } else {
            text_my_x.text = getText(R.string.no_permision)
            text_my_x.setTextColor(Color.RED)
            text_my_y.text = getText(R.string.no_permision)
            text_my_y.setTextColor(Color.RED)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = CompassPresenter()
        presenter.onAtach(this)
        button_latitude.setOnClickListener {
            val text: String = edit_latitude.text.toString()
            if (text.isNotBlank()) {
                presenter.setTargetLatitude(getDouble(text))
            }
        }
        button_longitude.setOnClickListener {
            val text: String = edit_longitude.text.toString()
            if (text.isNotBlank()) {
                presenter.setTargetLongitude(getDouble(text))
            }
        }
        edit_latitude.setOnEditorActionListener { textView, keyCode, keyEvent ->
            presenter.setTargetLatitude(getDouble(textView.text.toString()))
            true
        }
        edit_longitude.setOnEditorActionListener { textView, keyCode, keyEvent ->
            presenter.setTargetLongitude(getDouble(textView.text.toString()))
            true
        }


    }

    private fun getDouble(string: String): Double {
        return (string.toDoubleOrNull()) ?: return 0.0
    }


    override fun updateTarget(target: RotateAnimation) {
        indycator_arrow_view.startAnimation(target)//rotation = target.toFloat()
    }

    override fun updateNorth(rotation: RotateAnimation) {
        north_arrow_view.startAnimation(rotation)//rotation = rotation
    }

    @SuppressLint("SetTextI18n")
    override fun displatyMyLocation(x: Double, y: Double) {
        text_my_x.text = "x : $x"
        text_my_y.text = "y : $y"
    }

    @SuppressLint("SetTextI18n")
    override fun displayTargetLocation(x: Double, y: Double) {
        text_target_x.text = "x : $x"
        text_target_y.text = "y : $y"
    }
}