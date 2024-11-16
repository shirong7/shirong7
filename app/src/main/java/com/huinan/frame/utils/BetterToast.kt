package com.huinan.frame.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.huinan.frame.R

@SuppressLint("WrongConstant")
class BetterToast(var context: Context) {
    private var toast: Toast? = null
    private var textView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var contentView: View =
        LayoutInflater.from(context).inflate(R.layout.better_quick_toast, null)

    init {
        textView =
            contentView.findViewById(R.id.tv_text)
    }


    fun show(@StringRes message: Int) {
        show {
            textView.setText(message)
        }

    }

    fun show(message:String){
       show {
           textView.text = message
       }
    }


    private fun show(setTextFunc:()->Unit){
        toast?.apply {
            cancel()
        }
        handler.removeCallbacksAndMessages(null)
        toast = Toast(context)
        toast!!.apply {
            view = contentView
            setTextFunc.invoke()
            setGravity(Gravity.BOTTOM, 0, 45)
            duration = 1000
            handler.post {
                show()
            }
        }
    }


}