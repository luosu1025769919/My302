package com.luosu.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


/**
 * Created by User on 2017/3/7.
 */

class PrefUtils(context: Context, name: String) {

    internal var sp: SharedPreferences

    init {
        sp = context.getSharedPreferences(name, Activity.MODE_PRIVATE)


    }


    fun setString(key: String, value: String) {

        val editor = sp.edit()
        editor.putString(key, value)
        editor.commit()
        editor.clear()
    }

    fun getString(key: String): String {
        return sp.getString(key, "")

    }

    fun getString(key: String, default: String): String {
        return sp.getString(key, default)

    }

    fun setint(key: String, value: Int) {

        val editor = sp.edit()
        editor.putInt(key, value)
        editor.commit()
        editor.clear()
    }

    fun getint(key: String): Int {
        return sp.getInt(key, 0)

    }
    fun setLong(key: String, value: Long) {

        val editor = sp.edit()
        editor.putLong(key, value)
        editor.commit()
        editor.clear()
    }

    fun getLong(key: String): Long {
        return sp.getLong(key,0)

    }

    companion object {
        val userName = "userName"
        val USER_MESSAGES = "user_messages"
        val isFirst = "isFirst"
        val SID = "SID"



        val USERID = "USERID"
        val USERPASSWORD = "USERPASSWORD"
        val PASSWORD_TYPE = "PASSWORD_TYPE"
        val TOKEN = "TOKEN"
        val TOKEN_TIME = "TOKEN_TIME"
        val IS_BAITUI = "IS_BAITUI"

        val CONFIG_MESSAGE = "config_message"
        val DOMAIN = "domain"


        // 备用数据1
        private val RESEVER1 = "RESEVER1"
        // 备用数据2
        private val RESEVER2 = "RESEVER2"
    }
}
