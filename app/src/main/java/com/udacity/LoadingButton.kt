package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getText
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(
        context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0


    /*provides a timing engine for running animations which calculate
    the animated values & set them on the target objects.*/
    private val valueAnimator = ValueAnimator()


    /*This delegate is useful for when an action must be done
    each time button state changes.*/

    //takes the initial button value and a callback that’s called after button state changes

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(
            ButtonState.Completed) { p, old, new ->

    }


    private val paint = Paint().apply {
        isClickable = true
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    init {

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //canvas.drawRect(150f, 0f, 600f, 200f, paint)

        drawLoadingButton(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w), heightMeasureSpec, 0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    //method for drawing rectangular button when ButtonState.Loading

    private fun drawLoadingButton(canvas: Canvas) {

        //DRAW ACTUAL BUTTON
        //change button color
        paint.color = Color.BLUE

        //draw button
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        //draw the loading text

        //DRAW BUTTON TEXT

        //change text color to black

        paint.color = Color.BLACK

        canvas.drawText(resources.getString(R.string.button_loading), heightSize.toFloat(), heightSize
                .toFloat()/2,
                        paint)

    }

}