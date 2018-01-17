package com.luosu.utils

/**
 * Created by 落苏 on 2017/8/16.
 */

class Contents {
    object GameMessage {
        internal lateinit var GAMESDK_APPID: String

        internal lateinit var GAMESDK_APPKEY: String
        val SDK_VER = "3_9_11_AS3.0"
    }

    /**
     * 所有网络请求的act的集合
     */
    object Acts {

        //普通登录
        val LOGIN = "login"
        //检测更新
        val UPDATA = "updata"
        //普通注册
        val USER_REG = "reg2"
        //获取支付前的准备信息
        val CURRENCY_NUMBERS = "currency_numbers"
        //积分支付
        val BONUS = "bonus"
        //平台币支付
        val CURRENCY = "currency"
        //web版支付宝支付
        val ALIPAYWBWAP = "alipayWbWap"
        //web版微信支付
        val WEXINWBWAP = "WexinWbWap"

    }


    /**
     * activity跳转是的requestcode值
     */
    object ActivityJumpWhats {
        val FOURTH_JUMP_LOGIN = 101
        val FOURTH_JUMP_CHANG_PASS = 104
        val LOGIN_JUMP_REGISTER = 102

        val LOGIN_JUMP_FINDPASS = 103
        val ORDER_JUMP_ADDRESS = 105


    }

    /**
     * handelr的message的what值
     */

    object HandlerWhats {
        val FIRST_SLIDER = 101
        //用于注册界面验证码的倒计时
        val REGISTER_CODE = 102


    }

    companion object {
        val new_goods = "newgoods"
        val SOFT_NAME_SHOW_LENGTH = 18

        //网络请求时需要的key值
        val HTTP_SIGN_KEY = "QnxzMmVJZAC6XYmz18D"
        /**
         * token的有效时间
         */
        val Token_useful_time = (7 * 24 * 60 * 60 * 1000).toLong()
    }


}
