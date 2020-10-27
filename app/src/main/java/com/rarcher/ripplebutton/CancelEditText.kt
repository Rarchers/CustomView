package com.rarcher.ripplebutton

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import androidx.core.content.ContextCompat

class CancelEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {

    private val icon= ContextCompat.getDrawable(context,R.drawable.ic_baseline_cancel_24)

    var settingIcon : Drawable? = null
    var mWidth  = 0
    var mHeight = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
       event?.let {
           val x = event.x
           val y = event.y
           settingIcon?.let {
               if (x>mWidth-settingIcon!!.intrinsicWidth
                   && x<mWidth
                   && y>mHeight/2 - settingIcon!!.intrinsicHeight/2
                   && y<mHeight/2 + settingIcon!!.intrinsicHeight/2)
                   text?.clear()
           }
       }
        return super.onTouchEvent(event)
    }


    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        settingIcon = if (text.isNullOrEmpty()) null else icon
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,settingIcon,null)
    }





}