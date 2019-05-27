package com.acante.compassproject.ui.compass_ui

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.acante.compassproject.ui.compass_ui.CompassContract.*
import com.acante.compassproject.ui.compass_ui.ui_elements.TargetArrow


class CompassPresenter : Presenter, LocationListener {
    val TAG:String = "CompassPresenter"
    lateinit var view: View
    lateinit var context: Context
    var positionX:Double=0.0
    var posittionY:Double=0.0
    var targetArrow: TargetArrow

    constructor(context: Context) {
        this.context = context
        targetArrow = TargetArrow(5.0,5.0)

    }


    override fun onAtach(view: View) {
        this.view = view
    }

    override fun setTargetLongitude(longitude: Double) {
        posittionY = longitude
        updateTarget(positionX,posittionY)
    }

    override fun setTargetLatitude(latitude: Double) {
        positionX = latitude
        updateTarget(positionX,posittionY)
    }

    override fun onLocationChanged(location: Location?) {
       Log.d(TAG,"My location ${location.toString()}")
        updateTarget(location!!.latitude,location!!.longitude)
    }

    private fun updateTarget(latitude:Double,longitude:Double) {
        if (::view.isInitialized) {
            view.updateTarget(targetArrow.setTarget(latitude, longitude))
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(TAG,"My onStatusChanged    ${provider.toString()}")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(TAG,"My onProviderEnabled ${provider.toString()}")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(TAG,"My onProviderDisabled ${provider.toString()}")
    }


}