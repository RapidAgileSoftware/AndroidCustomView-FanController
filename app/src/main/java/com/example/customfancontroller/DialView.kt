package com.example.customfancontroller

import android.content.Context
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

private enum class FanSpeed(val label:Int){
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    High(R.string.fan_high)
}
private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35


class DialView @JvmOverloads constructor(
    context:Context,
    attrs:AttributeSet?=null,
    defStyleAttr:Int = 0
): View(context, attrs, defStyleAttr){

    // initialized here instead when the view is drawn, so it can run as fast as possible
    private var radius = 0.0f //radius of the circle
    private var fanSpeed = FanSpeed.OFF // active selection
    // position variable to draw label and indicator circle
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val point = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }


}