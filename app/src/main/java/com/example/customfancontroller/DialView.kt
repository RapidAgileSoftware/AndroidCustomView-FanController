package com.example.customfancontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


private enum class FanSpeed(val label:Int){
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);

    fun next() = when(this){
        OFF -> LOW
        LOW ->MEDIUM
        MEDIUM -> HIGH
        HIGH -> OFF
    }
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

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var fanSpeedLowColor = 0
    private var fanSpeedMediumColor = 0
    private var fanSpeedMaxColor = 0

    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.DialView) {
            fanSpeedLowColor = getColor(R.styleable.DialView_fanColor1,0)
            fanSpeedMediumColor = getColor(R.styleable.DialView_fanColor2, 0)
            fanSpeedMaxColor = getColor(R.styleable.DialView_fanColor3,0)
        }
    }
    // implement click action
    override fun performClick(): Boolean {
        // enables accessibility events and onClick Listener
        if(super.performClick()) return true
        // implement next fan speed
        fanSpeed = fanSpeed.next()
        // holds the current speed description
        contentDescription = resources.getString(fanSpeed.label)
        // trigger invalidate to redraw view
        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w,h)/ 2.0 *0.8).toFloat()
    }

    // calculate X & Y for the Speed Indicator
    private fun PointF.computeXYForSpeed(pos: FanSpeed, radius: Float) {
        // Angles are in radians.
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // set color depending if fan is on or off
        paint.color = when(fanSpeed){
            FanSpeed.OFF ->Color.GRAY
            FanSpeed.LOW -> fanSpeedLowColor
            FanSpeed.MEDIUM -> fanSpeedMediumColor
            FanSpeed.HIGH -> fanSpeedMaxColor
        }
        // draw circle for dial
        canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(),radius,paint)

        // draw the indicator
        val markerRadius = radius + RADIUS_OFFSET_INDICATOR
        pointPosition.computeXYForSpeed(fanSpeed,markerRadius)
        paint.color = Color.BLACK
        canvas.drawCircle(pointPosition.x, pointPosition.y,radius/12,paint)

        // draw the labels for speed
        val labelRadius =radius + RADIUS_OFFSET_LABEL
        for(i in FanSpeed.values()){
            pointPosition.computeXYForSpeed(i, labelRadius)
            val label = resources.getString(i.label)
            // make red dot if active
           paint.color = if(fanSpeed == i) Color.RED else Color.BLACK
            // draw text
            canvas.drawText(label, pointPosition.x, pointPosition.y , paint)
        }
    }

}