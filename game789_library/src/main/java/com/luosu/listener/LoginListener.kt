package com.luosu.listener

import com.luosu.bean.User

/**
 * Created by 落苏 on 2017/10/30.
 */
interface LoginListener {
    fun onSucces(user: User)
    fun onCancel()
    fun onError(code: Int, msg: String)
}