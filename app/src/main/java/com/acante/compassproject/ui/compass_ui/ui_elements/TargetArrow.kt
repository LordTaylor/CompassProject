package com.acante.compassproject.ui.compass_ui.ui_elements

class TargetArrow(var tsrgetX: Double, var trgetY: Double) {
    @Volatile
    var targetAngle: Double = 0.0
    private var myPositionX: Double = 0.0
    private var myPositionY: Double = 0.0

    fun setTarget(targetX: Double, targetY: Double) {
        this.trgetY = targetY
        this.tsrgetX = targetX
        setMyLocation()
    }

    fun setMyPosition(myPositionX: Double, myPositionY: Double){
        this.myPositionX = myPositionX
        this.myPositionY = myPositionY
        setMyLocation()
    }

    private fun setMyLocation(): Double {
        val xDist = tsrgetX - myPositionX
        val yDist = trgetY - myPositionY
        this.targetAngle = Math.toDegrees(Math.atan2(xDist, yDist))
        return targetAngle
    }

}