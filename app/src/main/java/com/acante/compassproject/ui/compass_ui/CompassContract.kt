package com.acante.compassproject.ui.compass_ui

import com.acante.compassproject.ui.base.BaseContract

interface CompassContract: BaseContract {
    interface View:BaseContract.View {
        fun updateTarget(target: Double)
    }

    interface Presenter:BaseContract.Presenter<View>{
        fun setTargetLongitude(longitude:Double)
        fun setTargetLatitude(latitude:Double)
    }
}