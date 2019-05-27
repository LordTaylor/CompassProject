package com.acante.compassproject.ui.base

interface BaseContract {
    interface View
    interface Presenter<in T>{
        fun onAtach(view:T)
    }
}