package com.rarcher.ripplebutton

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import kotlin.math.hypot

class ButtonView : androidx.appcompat.widget.AppCompatButton {

    var mIsPressed = false
    var revealRadius = 0.0f
    var centerX = 0.0f
    var centerY = 0.0f



    var showanimation = false


    val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )




    fun isValidClick(x: Float, y: Float):Boolean{
        return !(x<0 || x > width || y<0 || y>height)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> return isValidClick(event.x, event.y)
            MotionEvent.ACTION_UP -> {
                if (!isValidClick(event.x, event.y))
                    return false

                centerX = event.x
                centerY = event.y
                revealRadius = 0.0f

                showanimation = true;
                setFollowed(!mIsPressed, showanimation)
                return true
            }

        }
        return false
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun setFollowed(isFollowed: Boolean, needAnimation: Boolean){
        mIsPressed = isFollowed

        if (needAnimation){
            val animation = ObjectAnimator.ofFloat(
                this, "empty", 0.0f,
                hypot(width.toDouble(), height.toDouble()).toFloat()
            )
            animation.duration = 500
            animation.addUpdateListener {
                revealRadius = animation.animatedValue as Float
                invalidate()
            }

            animation.addListener(object:AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                   text = if (mIsPressed){
                       setTextColor(Color.WHITE)
                       setBackgroundColor(Color.RED)
                       "未关注"
                   }else{
                       setTextColor(Color.BLACK)
                       setBackgroundColor(Color.WHITE)
                       "关注"
                   }
                    showanimation = true
                    revealRadius = 0.0f
                    invalidate()
                }

                override fun onAnimationCancel(p0: Animator?) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    TODO("Not yet implemented")
                }
            })



          animation.start()
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!mIsPressed){
            paint.color = Color.WHITE
        }else {
            paint.color = Color.RED
        }
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(centerX,centerY,revealRadius,paint)


    }
}