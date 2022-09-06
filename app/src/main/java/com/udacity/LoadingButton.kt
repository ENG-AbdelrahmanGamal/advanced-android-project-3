package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var bgColor: Int = Color.BLACK
    private var textColor: Int = Color.BLACK
    private var progress: Double = 0.0
    private var valueAnimator = ValueAnimator()
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    }
    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()    // redraw the screen
        requestLayout() // when rectangular progress dimension changes
    }

    init {
        isClickable = true
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
//            // properties for downloading progress is defined
            R.animator.animator
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)

        // initialize custom attributes of the button
        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )
        try {

            bgColor = attr.getColor(
                R.styleable.LoadingButton_bgColor,
                ContextCompat.getColor(context, R.color.upload)
            )

            textColor = attr.getColor(
                R.styleable.LoadingButton_textColor,
                ContextCompat.getColor(context, R.color.white)
            )
        } finally {
            attr.recycle()
        }
    }


    private val rect = RectF(
        740f,
        60f,
        830f,
        140f
    )
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 0f
        paint.color = bgColor

        // draw custom button
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        if (buttonState == ButtonState.Loading) {
            paint.color = Color.parseColor("#004349")
            canvas.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )
            // draw circle end of text button
            paint.color = Color.parseColor("#F9A825")
            canvas.drawArc(rect, 0f, (360 * (progress / 100)).toFloat(), true, paint)

        }

        val buttonText = if (buttonState == ButtonState.Loading)
            resources.getString(R.string.lodingfile)

        else resources.getString(R.string.download)

        paint.color = textColor
        canvas.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER // button text alignment
        textSize = 55.0f //button text size
        typeface = Typeface.create("", Typeface.BOLD) // button text's font style
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        animation()
        return true
    }

    private fun animation() {
        valueAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}