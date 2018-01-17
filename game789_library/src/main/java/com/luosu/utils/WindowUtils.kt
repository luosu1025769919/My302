package com.luosu.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.luosu.game789_library.R
import com.luosu.listener.MyWindowBtnListener

/**
 * Created by 落苏 on 2018/1/3.
 */
class WindowUtils(activity: Activity) {
    internal lateinit var window: PopupWindow
    internal lateinit var dialog: AlertDialog
    internal var activity: Activity = activity

    internal fun showWindow(title: String, msg: String, parent: View, listener: MyWindowBtnListener) {
        var view = View.inflate(activity, R.layout.window, null)
        var tv_title: TextView = view.findViewById(R.id.window_title) as TextView
        var tv_msg: TextView = view.findViewById(R.id.window_msg) as TextView
        var btn_false: Button = view.findViewById(R.id.window_false) as Button
        var btn_true: Button = view.findViewById(R.id.window_true) as Button
        tv_title.setText(title)
        tv_msg.setText(msg)
        btn_false.setOnClickListener({
            listener.onFalseListener(btn_false)
        })
        btn_true.setOnClickListener({
            listener.onTrueListener(btn_true)
        })
        window = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.isFocusable = false
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00FFFFFF")))
        window.isOutsideTouchable = false
        window.showAtLocation(parent, Gravity.CENTER, 0, 0)

    }

    internal fun dissmissWindow() {
        if (window.isShowing) {
            window.dismiss()
        }

    }

    internal fun showDialog(title: String) {
        dialog = ProgressDialog(activity)
//        dialog.setTitle(title)
        dialog.setMessage(title)


        dialog.show()
    }

    internal fun dissmissDialog() {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}