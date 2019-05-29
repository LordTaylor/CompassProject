package com.acante.compassproject.ui.compass_ui

import android.view.animation.RotateAnimation
import com.acante.compassproject.ui.base.BaseContract

interface CompassContract: BaseContract {
    interface View:BaseContract.View {
        fun updateTarget(target: RotateAnimation)
        fun updateNorth(rotation: RotateAnimation)
        fun displayMyLocation(x:Double, y:Double)
        fun displayTargetLocation(x:Double,y:Double)
    }

    interface Presenter:BaseContract.Presenter<View>{
        fun setTargetLongitude(longitude: String)
        fun setTargetLatitude(latitude: String)
    }
}