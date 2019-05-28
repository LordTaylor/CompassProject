package com.acante.compassproject.ui.compass_ui.ui_elements

class TargetArrow(var tsrgetX: Double, var trgetY: Double) {

    var targetAngle: Double = 0.0
        get() = targetAngle
    var currentAngle: Double = 0.0

    var myPositionX: Double = 0.0
    var myPositionY: Double = 0.0

    var roatationEasing = 10.0

    fun setTarget(targetX: Double, targetY: Double) {
        this.trgetY = targetY
        this.tsrgetX = targetX
    }

    fun setMyLocation(myPositionX: Double, myPositionY: Double): Double {
        this.myPositionX = myPositionX
        this.myPositionY = myPositionY

        var xDist = tsrgetX - myPositionX
        var yDist = trgetY - myPositionY

        this.targetAngle = Math.toDegrees(Math.atan2(xDist, yDist))

        return targetAngle
    }


}