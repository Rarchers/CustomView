package com.rarcher.ripplebutton

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.math.hypot


class RippleButton : FrameLayout{

    var isFollow = false
    var isFirstInit = true
    var revealRadius = 0.0f
    var centerX = 0.0f
    var centerY = 0.0f

    lateinit var follow : TextView
    lateinit var unfollow : TextView

    val mPath = Path()


    constructor(context: Context) : super(context){init()}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){init()}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){init()}


    fun isValidClick(x: Float, y: Float):Boolean{
        return !(x<0 || x > width || y<0 || y>height)
    }


    fun init(){
        follow = TextView(context)
        follow.text = "未关注"
        follow.setTextColor(Color.BLACK)
        follow.gravity = 17
        follow.setBackgroundColor(Color.WHITE)
        follow.isSingleLine = true
        addView(follow)

        unfollow = TextView(context)
        unfollow.text = "关注"
        unfollow.setTextColor(Color.WHITE)
        unfollow.gravity = 17
        unfollow.setBackgroundColor(Color.RED)
        unfollow.isSingleLine = true
        addView(unfollow)

        follow.setPadding(40, 40, 40, 40)
        unfollow.setPadding(40, 40, 40, 40)
        setFollowed(isFollowed = false, needAnimation = false)

    }


    @SuppressLint("ObjectAnimatorBinding")
    fun setFollowed(isFollowed: Boolean, needAnimation: Boolean){
        isFollow = isFollowed
        if (isFollowed){
            follow.visibility = View.VISIBLE
            unfollow.visibility = View.VISIBLE
            follow.bringToFront()
        }else{
            follow.visibility = View.VISIBLE
            unfollow.visibility = View.VISIBLE
            unfollow.bringToFront()
        }
        if (needAnimation){
            val animation = ObjectAnimator.ofFloat(
                follow, "empty", 0.0f,
                hypot(width.toDouble(), height.toDouble()).toFloat()
            )
            animation.duration = 500
            animation.addUpdateListener {
                revealRadius = animation.animatedValue as Float
                invalidate()
            }
            animation.start()
        }

    }

    override fun drawChild(canvas: Canvas, paramView: View?, paramLong: Long): Boolean {
        if (drawBackground(paramView!!)) {
            return super.drawChild(canvas, paramView, paramLong)
        }
        val i: Int = canvas.save()
        mPath.reset()
        mPath.addCircle(centerX, centerY, revealRadius, Path.Direction.CW)
        canvas.clipPath(this.mPath)
        val bool2 = super.drawChild(canvas, paramView, paramLong)
        canvas.restoreToCount(i)
        return bool2
    }


    fun drawBackground(paramView: View) =
        if (isFirstInit) true
        else if (isFollow && paramView == unfollow) true
        else !isFollow && paramView == follow


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> return isValidClick(event.x, event.y)
            MotionEvent.ACTION_UP -> {
                if (!isValidClick(event.x, event.y))
                    return false
                isFirstInit = false
                centerX = event.x
                centerY = event.y
                revealRadius = 0.0f
                follow.visibility = View.VISIBLE
                unfollow.visibility = View.VISIBLE
                setFollowed(!isFollow, true)
                return true
            }

        }
        return false
    }
}