package com.acante.compassproject.ui.compass_ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.util.Log
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = CompassPresenter(context!!)
        presenter.onAtach(this)

        var locationManager: LocationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, presenter)
        }
        button_latitude.setOnClickListener {
            var text:String = edit_latitude.text.toString()
            if(text.isNotBlank()){
                presenter.setTargetLatitude(text.toDouble())
            }
        }
        button_longitude.setOnClickListener {
            var text:String = edit_longitude.text.toString()
            if(text.isNotBlank()){
                presenter.setTargetLongitude(text.toDouble())
            }
        }
    }

    override fun updateTarget(target: Double) {
        indycator_arrow_view.rotation = target.toFloat()
        Log.d(TAG,"angle $target")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}