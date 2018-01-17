package com.luosu.utils

import android.util.Log

/**
 * * @author 作者 : 落苏

 * @date 创建时间：2016年3月22日 下午3:20:50
 */

object UkLogUtil {
    var flag = true
    private val LOG_MAXLENGTH = 2000
    fun i(message: String) {
        if (flag) {
            Log.i("tag**", message)
        }
    }

    fun i(tag: String, message: String) {
        if (flag) {
            Log.i(tag, message)
        }
    }

    fun w(tag: String, message: String) {
        if (flag) {
            Log.w(tag, message)
        }
    }

    fun w(tag: String, message: String, tr: Throwable) {
        if (flag) {
            Log.w(tag, message, tr)
        }
    }

    fun v(tag: String, message: String) {
        if (flag) {
            Log.v(tag, message)
        }
    }

    fun d(tagName: String, msg: String) {
        if (flag) {
            val strLength = msg.length
            var start = 0
            var end = LOG_MAXLENGTH
            for (i in 0..99) {
                if (strLength > end) {
                    Log.d(tagName + i, msg.substring(start, end))
                    start = end
                    end = end + LOG_MAXLENGTH
                } else {
                    Log.d(tagName + i, msg.substring(start, strLength))
                    break
                }
            }
        }
    }

    fun e(tag: String, message: String, tr: Throwable) {
        if (flag) {
            Log.e(tag, message, tr)
        }
    }

    fun e(tag: String, message: String) {
        if (flag) {
            Log.e(tag, message)
        }
    }
}
