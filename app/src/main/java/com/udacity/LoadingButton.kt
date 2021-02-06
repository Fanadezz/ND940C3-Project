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

    /*provides a timing engine for running animations which calculate
    the animated values & set them on the target objects.*/
    private val valueAnimator = ValueAnimator()
    private  var animatedWidthValue:Float =0.0f

    //takes the initial button value and a callback that’s called after button state changes

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(
            ButtonState.Completed) { p, old, new ->

        if(new==ButtonState.Loading){
Timber.i("Animation Triggered")
            animateLoadingButton()
        }
        //force draw()
        invalidate()

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
        Timber.i("OnClick Called and ButtonState is $buttonState")

        return super.performClick()


   /*     if (super.performClick()) return true
        buttonState = ButtonState.Loading
Timber.i("OnClick Called and ButtonState is $buttonState")

        return true*/
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

        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(),paint)

        //write button text
        paint.color = Color.WHITE
        drawButtonText(resources.getString(R.string.default_button_text), canvas)
    }

    //method for drawing rectangular button when ButtonState.Loading

    private fun drawLoadingButton(canvas: Canvas) {


        //change button color
        paint.color = primaryColor

        //draw button
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        //write button text
        paint.color = Color.WHITE
        drawButtonText(resources.getString(R.string.button_loading), canvas)

        //animateLoadingButton(canvas)

    }


    private fun animateLoadingButton(){

        paint.color = darkPrimaryColor

        valueAnimator.apply {
            interpolator = LinearInterpolator()
            //animate button width from 0 to width size
            setFloatValues(0f, widthSize.toFloat())
            duration = 5000



        }
        valueAnimator.addUpdateListener {

           animatedWidthValue = it.animatedValue as Float
          //  canvas.drawRect(0f, 0f, animatedWidthValue, heightSize.toFloat(), paint)
            invalidate()
        }

        valueAnimator.start()
    }




    private fun drawButtonText(text: String, canvas: Canvas) {
        //DRAW BUTTON TEXT

        //change text color to black


        canvas.drawText(
                text, (widthSize / 2).toFloat(),
                (heightSize/2 )+(heightSize/10).toFloat(), paint)
    }


    //called every time your view needs to be drawn or redrawn
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)




    when(buttonState){

        ButtonState.Loading -> drawLoadingButton(canvas)
        ButtonState.Completed -> drawDefaultButton(canvas)
        else -> drawDefaultButton(canvas)
    }

        paint.color = darkPrimaryColor
        canvas.drawRect(0f, 0f, animatedWidthValue, heightSize.toFloat(), paint)

    }


}