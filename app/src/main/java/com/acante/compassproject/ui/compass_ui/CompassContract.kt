package com.acante.compassproject.ui.compass_ui

import android.view.animation.RotateAnimation
import com.acante.compassproject.ui.base.BaseContract

interface CompassContract: BaseContract {
    interface View:BaseContract.View {
        fun updateTarget(target: Double)
        fun updateNorth(rotation: Float)
        fun displatyMyLocation(x:Double,y:Double)
        fun displayTargetLocation(x:Double,y:Double)
    }

    interface Presenter:BaseContract.Presenter<View>{
        fun setTargetLongitude(longitude:Double)
        fun setTargetLatitude(latitude:Double)
    }
}