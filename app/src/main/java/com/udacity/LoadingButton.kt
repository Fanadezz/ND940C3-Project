package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.content.res.ResourcesCompat
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(
        context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    //variable for primary and primaryDark Color
    private val primaryColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    private val darkPrimaryColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    private val accentColor = ResourcesCompat.getColor(resources, R.color.colorAccent, null)

    /*provides a timing engine for running animations which calculate
    the animated values & set them on the target objects.*/
    private val valueAnimatorWidth = ValueAnimator()
    private val valueAnimatorAngle = ValueAnimator()
    private var animatedWidth: Float = 0.0f
    private var animatedAngle: Float = 0.0f

    //takes the initial button value and a callback that’s called after button state changes

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(
            ButtonState.Completed) { p, old, new ->

        if (new == ButtonState.Loading) {
            Timber.i("Animation Triggered")

            animateLoadingButton()
            animateArcAngle()
        }


    }


    private val paint = Paint().apply {


        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 45f
    }

    init {

    }

    override fun performClick(): Boolean {
        buttonState = ButtonState.Loading

        return super.performClick()


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

    private fun drawDefaultButton(canvas: Canvas) {

        paint.color = primaryColor

        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        //write button text
        paint.color = Color.WHITE
        drawText(resources.getString(R.string.default_button_text), canvas)
    }

    //method for drawing rectangular button when ButtonState.Loading

    private fun drawLoadingButton(canvas: Canvas) {


        //change button color
        paint.color = primaryColor

        //draw button
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)




    }


    private fun animateLoadingButton() {

        paint.color = darkPrimaryColor

        valueAnimatorWidth.apply {
            interpolator = LinearInterpolator()
            //animate button width from 0 to width size
            setFloatValues(0f, widthSize.toFloat())
            duration = 3000


        }
        valueAnimatorWidth.addUpdateListener {

            animatedWidth = it.animatedValue as Float
            //  canvas.drawRect(0f, 0f, animatedWidthValue, heightSize.toFloat(), paint)
            invalidate()
        }

        valueAnimatorWidth.start()
    }


    private fun drawText(text: String, canvas: Canvas) {

        paint.color = Color.WHITE
        canvas.drawText(
                text, (widthSize / 2).toFloat(), (heightSize / 2) + (heightSize / 10).toFloat(), paint)
    }


    //called every time your view needs to be drawn or redrawn
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

       if (buttonState==ButtonState.Completed){

           drawDefaultButton(canvas)
       }else{

           drawLoadingButton(canvas)
           drawAnimatedBackground(canvas)
           drawText(resources.getString(R.string.button_loading), canvas)
           drawSmallCircle(canvas)
       }







    }

    private fun drawAnimatedBackground(canvas: Canvas) {
        //draw animated background for the Load Button
        paint.color = darkPrimaryColor
        canvas.drawRect(0f, 0f, animatedWidth, heightSize.toFloat(), paint)
    }

    private fun drawSmallCircle(canvas: Canvas) {
        //draw Arc
        paint.color = accentColor
        canvas.drawArc(
                (widthSize * 0.66).toFloat(), (heightSize * 0.2).toFloat(), (widthSize * 0.74).toFloat(),
                (heightSize * 0.8).toFloat(), 0f, animatedAngle, true, paint)


       // canvas.drawa
        //canvas.drawCircle(0f, 0f, ((heightSize/2).toFloat()), paint)
    }


    private fun animateArcAngle() {
        valueAnimatorAngle.apply {

            duration = 3000
            //set range of 0 to 360 degrees clockwise
            setFloatValues(0f, 360f)
            interpolator = LinearInterpolator()


        }

        //add listener
        valueAnimatorAngle.addUpdateListener {

            //set value of the new angle
            animatedAngle = it.animatedValue as Float

            //change button state when animation is over
            if (animatedAngle==360f){

                buttonState = ButtonState.Completed
            }
            invalidate()
        }



        //start animation
        valueAnimatorAngle.start()
    }

}