package com.acante.compassproject.ui.compass_ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.acante.compassproject.R
import kotlinx.android.synthetic.main.compass_fragment.*


class CompassFragment : Fragment(), CompassContract.View {
    val TAG = "CompassFragment"
    lateinit var locationProvider: LocationProvider

    lateinit var presenter: CompassPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.compass_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
//        setUpSensors()


    }

    override fun onPause() {
        super.onPause()
//        var sensorManager: SensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        sensorManager.unregisterListener(presenter)
//        presenter.stopWorker()
    }

    private fun setUpSensors() {
        var locationManager: LocationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var sensorManager: SensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
        }
        var intent:Intent = Intent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        presenter = CompassPresenter(context!!)
//        presenter.onAtach(this)
        button_latitude.setOnClickListener {
            var text: String = edit_latitude.text.toString()
            if (text.isNotBlank()) {
//                presenter.setTargetLatitude(text.toDouble())
            }
        }
        button_longitude.setOnClickListener {
            var text: String = edit_longitude.text.toString()
            if (text.isNotBlank()) {
//                presenter.setTargetLongitude(text.toDouble())
            }
        }
    }


    override fun updateTarget(target: Double) {
        indycator_arrow_view.rotation = target.toFloat()
    }

    override fun updateNorth(rotation: Float) {
        north_arrow_view.rotation = rotation
    }

    override fun displatyMyLocation(x: Double, y: Double) {
        text_my_x.text = "x : $x"
        text_my_y.text = "y : $y"
    }

    override fun displayTargetLocation(x: Double, y: Double) {
        text_target_x.text = "x : $x"
        text_target_y.text = "y : $y"
    }
}