package com.luosu.sdk

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.luosu.activitys.LoginActivity
import com.luosu.activitys.PayActivity
import com.luosu.activitys.SimpleLoginActivity
import com.luosu.activitys.UpdataActivity
import com.luosu.listener.LoginListener
import com.luosu.listener.PayListener
import com.luosu.listener.UpdataListener
import com.luosu.utils.*
import org.xutils.x

/**
 * Created by 落苏 on 2017/10/26.
 */
object Game789Sdk {
    private lateinit var context: Context
    internal lateinit var loginListener: LoginListener
    internal lateinit var payListener: PayListener
    internal lateinit var updataListener: UpdataListener


    fun initContext(context: Context) {

        this.context = context
        Contents.GameMessage.GAMESDK_APPID = BaseUtils.getMetaData(context, "GAMESDK_APPID")
        Contents.GameMessage.GAMESDK_APPKEY = BaseUtils.getMetaData(context, "GAMESDK_APPKEY")
        if (TextUtils.isEmpty(Contents.GameMessage.GAMESDK_APPID) || TextUtils.isEmpty(Contents.GameMessage.GAMESDK_APPID)) {
            throw  UkgameNullPointThrowable("id or key no init")
        }
    }

    fun initContext(context: Context, appid: String, appkey: String) {
        this.context = context
        Contents.GameMessage.GAMESDK_APPID = appid
        Contents.GameMessage.GAMESDK_APPKEY = appkey

        if (TextUtils.isEmpty(Contents.GameMessage.GAMESDK_APPID) || TextUtils.isEmpty(Contents.GameMessage.GAMESDK_APPID)) {
            throw  UkgameNullPointThrowable("id or key no init")
        }
    }

    /**
     * 更新接口
     */
    fun updata(listener: UpdataListener) {
        this.updataListener = listener
        var intent: Intent = Intent(context, UpdataActivity::class.java)
        context.startActivity(intent)
    }

    fun login(listener: LoginListener) {
        this.loginListener = listener
        var intent: Intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    fun ukLogin(third_id: String?, listener: LoginListener) {
        this.loginListener = listener
        var intent: Intent = Intent(context, SimpleLoginActivity::class.java)
        intent.putExtra("third_id",third_id)
        context.startActivity(intent)
    }

    fun pay(goodName: String, price: String, sid: String, custom: String, listener: PayListener) {

        this.payListener = listener
        var token = PrefUtils(context, PrefUtils.USER_MESSAGES).getString(PrefUtils.TOKEN)
        var token_time = PrefUtils(context, PrefUtils.USER_MESSAGES).getLong(PrefUtils.TOKEN_TIME)

        if (!TextUtils.isEmpty(token) && System.currentTimeMillis() - token_time <= Contents.Token_useful_time) {
            var intent: Intent = Intent(context, PayActivity::class.java)
            intent.putExtra("goodName", goodName)
            intent.putExtra("price", price)
            intent.putExtra("sid", sid)
            intent.putExtra("custom", custom)
            PrefUtils(context, PrefUtils.USER_MESSAGES).setString(PrefUtils.SID, sid)
            context.startActivity(intent)
        } else {
            throw  UkgameNotLoginThrowable("you not login")
        }
    }


}