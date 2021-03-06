package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(context: Context,
                                              attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    //width and height variables
    private var widthSize = 0
    private var heightSize = 0

    //custom attributes
    private var backGroundColor = 0
    private var textColor = 0
    private var defaultButtonColor = 0
    private var accentColor = 0

    /*provides a timing engine for running animations which calculate
    the animated values & set them on the target objects.*/
    private val valueAnimatorWidth = ValueAnimator()
    private val valueAnimatorAngle = ValueAnimator()
    private var animatedWidth: Float = 0.0f
    private var animatedAngle: Float = 0.0f


    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            backGroundColor = getColor(R.styleable.LoadingButton_defaultBackGroundColor,0)
            textColor = getColor(R.styleable.LoadingButton_defaultTextColor, 0)
            accentColor = getColor(R.styleable.LoadingButton_accentButtonColor,0)
            defaultButtonColor = getColor(R.styleable.LoadingButton_defaultButtonColor,0)

        }
    }

    //takes the initial button value and a callback that is called after button state changes

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->

        if (new == ButtonState.Loading) {
            Timber.i("Animation Triggered")

            animateLoadingButton()
            animateArcAngle()
        }
    }

    //PAINT OBJECT
    private val paint = Paint().apply {

        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 45f
    }

    //ONCLICK
    override fun performClick(): Boolean {
        buttonState = ButtonState.Loading
        return super.performClick()
    }

    //ON_MEASURE
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    //ON_DRAW
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        //draw default button
        if (buttonState == ButtonState.Completed) {
            drawDefaultButton(canvas)
        }

        //else draw animations
        else {
            drawLoadingButton(canvas)
            drawAnimatedBackground(canvas)
            drawText(resources.getString(R.string.button_loading), canvas)
            drawSmallCircle(canvas)
        }

    }

    //DEFAULT_BUTTON
    private fun drawDefaultButton(canvas: Canvas) {
        paint.color = defaultButtonColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        //write button text
        paint.color = Color.WHITE
        drawText(resources.getString(R.string.default_button_text), canvas)
    }

    //LOADING_BUTTON
    private fun drawLoadingButton(canvas: Canvas) {
        //change button color
        paint.color = defaultButtonColor

        //draw button
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

    }

    //LOADING_BUTTON_BACKGROUND
    private fun drawAnimatedBackground(canvas: Canvas) {
        //draw animated background for the Load Button
        paint.color = backGroundColor
        canvas.drawRect(0f, 0f, animatedWidth, heightSize.toFloat(), paint)
    }

    //ANIMATE_LOADING_BUTTON_BACKGROUND
    private fun animateLoadingButton() {

        //set animation parameters
        valueAnimatorWidth.apply {

            interpolator = LinearInterpolator()
            //animate button width from 0 to the width size
            setFloatValues(0f, widthSize.toFloat())
            duration = 2500
        }

        //listener
        valueAnimatorWidth.addUpdateListener {
            //update width
            animatedWidth = it.animatedValue as Float

            //force draw()
            invalidate()
        }

        valueAnimatorWidth.start()
    }


    //SMALL_CIRCLE
    private fun drawSmallCircle(canvas: Canvas) {

        paint.color = accentColor

        //draw Arc
        canvas.drawArc((widthSize * 0.66).toFloat(),
                       (heightSize * 0.2).toFloat(),
                       (widthSize * 0.74).toFloat(),
                       (heightSize * 0.8).toFloat(),
                       0f,
                       animatedAngle,
                       true,
                       paint)
    }

    //SMALL_CIRCLE_ANIMATION
    private fun animateArcAngle() {
        valueAnimatorAngle.apply {

            duration = 2500
            //set range of 0 to 360 degrees clockwise
            setFloatValues(0f, 360f)
            interpolator = LinearInterpolator()

        }

        //add listener
        valueAnimatorAngle.addUpdateListener {

            //set value of the new angle
            animatedAngle = it.animatedValue as Float

            //change button state when animation is over
            if (animatedAngle == 360f) {

                buttonState = ButtonState.Completed
            }
            invalidate()
        }

        //start animation
        valueAnimatorAngle.start()
    }


    //DRAW_TEXT_ON_BUTTONS
    private fun drawText(text: String, canvas: Canvas) {

        paint.color = textColor
        canvas.drawText(text,
                        (widthSize / 2).toFloat(),
                        (heightSize / 2) + (heightSize / 10).toFloat(),
                        paint)
    }

}