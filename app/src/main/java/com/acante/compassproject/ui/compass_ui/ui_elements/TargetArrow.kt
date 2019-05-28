package com.acante.compassproject.ui.compass_ui.ui_elements

import java.text.FieldPosition

class TargetArrow(var positionX: Double, var positionY: Double) {

    var targetAngle: Double = 0.0
    var currentAngle: Double = 0.0
    var roatationEasing = 10.0

    fun setTarget(targetX: Double, targetY: Double) {
        positionY=targetY
        positionX=targetX
    }

    fun setMyLocation(myPositionX:Double,myPositionY: Double): Double{
        var xDist =  positionX -myPositionX
        var yDist =  positionY -myPositionY

        this.targetAngle = Math.atan2(yDist, xDist) - Math.PI / 2

        this.currentAngle =
            if (Math.abs(this.currentAngle) > Math.PI * 2) if (this.currentAngle < 0) this.currentAngle % Math.PI * 2 + Math.PI * 2 else this.currentAngle % Math.PI * 2 else this.currentAngle
        this.targetAngle =
            this.targetAngle + getAngle()
        this.currentAngle =
            this.currentAngle + (this.targetAngle - this.currentAngle) / roatationEasing  // give easing when rotation comes closer to the target point


        return Math.toDegrees(Math.atan2(xDist,yDist) )
    }

    private fun getAngle() :Double{
        if (Math.abs(this.targetAngle - this.currentAngle) < Math.PI) {
           return  0.0
        }else if (this.targetAngle - this.currentAngle > 0) {
           return -Math.PI * 2
        }else{
            return Math.PI * 2
        }
    }

}