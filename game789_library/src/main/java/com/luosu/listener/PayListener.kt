package com.luosu.listener

import com.luosu.bean.User

/**
 * Created by 落苏 on 2018/1/2.
 */
interface PayListener {
    fun onPayResult(isSuccess:Boolean)
    fun onCancel()
    fun onError(code: Int, msg: String)
}