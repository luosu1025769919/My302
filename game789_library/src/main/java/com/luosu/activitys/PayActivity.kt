package com.luosu.activitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.luosu.game789_library.R
import com.luosu.listener.MyWindowBtnListener
import com.luosu.listener.PayListener
import com.luosu.sdk.Game789Sdk
import com.luosu.utils.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class PayActivity : AppCompatActivity(), View.OnClickListener {


    internal lateinit var api: IWXAPI
    internal lateinit var btnZhifubao: LinearLayout
    internal lateinit var btnWeixin: LinearLayout
    internal lateinit var btnPlatform: LinearLayout
    internal lateinit var btnJifen: LinearLayout
    internal lateinit var btn_back: Button
    internal lateinit var tv_account: TextView
    internal lateinit var tv_platform: TextView
    internal lateinit var tv_gold: TextView
    internal lateinit var tv_jifen: TextView
    internal lateinit var pay: View
    internal lateinit var payListener: PayListener
    internal lateinit var goodName: String;
    internal lateinit var price: String;
    internal lateinit var sid: String;
    internal lateinit var custom: String;

    internal lateinit var is_bind_mobile: String
    internal lateinit var way_paytype: String
    internal lateinit var promote_id: String
    internal lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        payListener = Game789Sdk.payListener
        token = PrefUtils(this@PayActivity, PrefUtils.USER_MESSAGES).getString(PrefUtils.TOKEN)
        initIntent()
        initViews()
        setData()
        BaseUtils.addActivity("payactivity", this@PayActivity)

    }

    private fun initIntent() {
        var intent = this.intent;
        goodName = intent.getStringExtra("goodName")
        price = intent.getStringExtra("price")
        sid = intent.getStringExtra("sid")
        custom = intent.getStringExtra("custom")

    }

    private fun setData() {
        tv_account.setText(PrefUtils(this@PayActivity, PrefUtils.USER_MESSAGES).getString(PrefUtils.userName))
        tv_gold.setText(price)

        tv_account.post({
            initTv_Data()

        })
    }


    /**
     * 从服务端拉取数积分，平台币数据，是否需要绑定手机，以及是被绑定到推广员id上，以及支付的方式
     */
    private fun initTv_Data() {
        var httpUtils: MyHTTPutils = MyHTTPutils();
        var map: TreeMap<String, String> = TreeMap <String, String>();
        map.put("p_token", PrefUtils(this@PayActivity, PrefUtils.USER_MESSAGES).getString(PrefUtils.TOKEN))
        httpUtils.httpPost(this@PayActivity, map, Contents.Acts.CURRENCY_NUMBERS, object : MyHTTPutils.ResultListener {
            override fun onResult(result: String) {
                try {
                    var jo: JSONObject = JSONObject(result)
                    var result_int = jo.getInt("result")

                    if (result_int == 1) {
                        var data = jo.getJSONObject("data")
                        tv_platform.setText(data.getString("currency_numbers"))
                        tv_jifen.setText(data.getString("bonus"))
                        is_bind_mobile = data.getString("is_bind_mobile")
                        way_paytype = data.getString("way_paytype")
                        promote_id = data.getString("promote_id")
                        if ("0".equals(is_bind_mobile)) {
                            remind_user_bind();
                        }
                    } else {
                        toast(jo.getString("msg"))
                    }
                } catch(e: JSONException) {
                    toast("数据异常")
                    payListener.onError(1, "数据异常")
                    finish()
                }
            }

            override fun onError(result: String) {
                payListener.onError(2, "初始化失败")
                finish()
            }

        })
    }

    /**
     * 服务端判定需要绑定手机，
     */
    private fun remind_user_bind() {

    }

    fun Activity.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    fun initViews() {
        btnZhifubao = findViewById(R.id.btn_zhifubao_pay) as LinearLayout
        btnWeixin = findViewById(R.id.btn_weixin_pay) as LinearLayout
        btnJifen = findViewById(R.id.btn_jifen_pay) as LinearLayout
        btnPlatform = findViewById(R.id.btn_platform_pay) as LinearLayout
        tv_account = findViewById(R.id.tv_account) as TextView
        tv_platform = findViewById(R.id.tv_platform) as TextView
        tv_gold = findViewById(R.id.tv_gold) as TextView
        tv_jifen = findViewById(R.id.tv_jifen) as TextView
        pay = findViewById(R.id.pay)
        btn_back = findViewById(R.id.btn_back) as Button

        registerListener()
    }

    private fun registerListener() {
        btnZhifubao.setOnClickListener(this@PayActivity)
        btnWeixin.setOnClickListener(this@PayActivity)
        btnPlatform.setOnClickListener(this@PayActivity)
        btnJifen.setOnClickListener(this@PayActivity)
        btn_back.setOnClickListener(this@PayActivity)
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.btn_zhifubao_pay -> {
                zhifubaopay()

            }
            v?.id == R.id.btn_weixin_pay -> {
                if (BaseUtils.weiXinIsInstall(this@PayActivity)) {
                    if ("0".equals(way_paytype)) {
                        weixinWebPay()
                    } else {
                        weixinPay()
                    }
                } else {
                    toast("您还没有安装微信！")
                }
            }
            v?.id == R.id.btn_jifen_pay -> {
                jifenPay()
            }
            v?.id == R.id.btn_platform_pay -> {
                platformPay()
            }
            v?.id == R.id.btn_back -> {
                payListener.onCancel()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        payListener.onCancel()
        finish()

    }

    /**
     * 支付宝web版支付
     */
    private fun zhifubaopay() {
        val params = TreeMap<String, String>()
        params.put("p_total_fee", price)
        params.put("p_token", token)
        params.put("p_subject", goodName)
        params.put("p_custom", custom)
        params.put("p_promote_id", promote_id)
        var window = WindowUtils(this@PayActivity)
        window.showDialog("调起支付宝支付")
        MyHTTPutils().httpPost(this@PayActivity, params, Contents.Acts.ALIPAYWBWAP, object : MyHTTPutils.ResultListener {
            override fun onResult(result: String) {
                window.dissmissDialog()
                try {
                    var jo = JSONObject(result)
                    var result_int = jo.getInt("result")
                    var msg = jo.getString("msg")
                    if (result_int == 1) {
                        var data = jo.getJSONObject("data")
                        val url = data.getString("alipay_pay_url")
                        val intent = Intent(this@PayActivity, WebPayActivity::class.java)
                        intent.putExtra("url", url)
                        intent.putExtra("pay_way", "alipay")

                        startActivity(intent)
                    } else {
                        toast(msg)
                    }
                } catch(e: JSONException) {
                    toast("数据异常")
                }

            }

            override fun onError(result: String) {
                window.dissmissDialog()
                toast("网络异常")

            }

        })
    }

    /**
     * 微信web版支付
     */
    private fun weixinWebPay() {
        val params = TreeMap<String, String>()
        params.put("p_total_fee", price)
        params.put("p_token", token)
        params.put("p_subject", goodName)
        params.put("p_custom", custom)
        params.put("p_promote_id", promote_id)
        var window = WindowUtils(this@PayActivity)
        window.showDialog("调起微信支付")
        MyHTTPutils().httpPost(this@PayActivity, params, Contents.Acts.WEXINWBWAP, object : MyHTTPutils.ResultListener {
            override fun onResult(result: String) {
                window.dissmissDialog()
                try {
                    var jo = JSONObject(result)
                    var result_int = jo.getInt("result")
                    var msg = jo.getString("msg")
                    if (result_int == 1) {
                        var data = jo.getJSONObject("data")
                        val url = data.getString("wb_pay_url")
                        val intent = Intent(this@PayActivity, WebPayActivity::class.java)
                        intent.putExtra("url", url)
                        intent.putExtra("pay_way", "wxpay")

                        startActivity(intent)
                    } else {
                        toast(msg)
                    }
                } catch(e: JSONException) {
                    toast("数据异常")
                }

            }

            override fun onError(result: String) {
                window.dissmissDialog()
                toast("网络异常")

            }

        })
    }

    /**
     * 微信原生支付
     */
    private fun weixinPay() {

    }

    /**
     * 积分支付
     */
    private fun jifenPay() {
        var window = WindowUtils(this@PayActivity)
        window.showWindow("积分支付", "积分是由游酷官方对玩家提供的支付方式,您确定开始支付吗？", pay, object : MyWindowBtnListener {
            override fun onTrueListener(v: View) {
                window.dissmissWindow()
                var params = TreeMap<String, String>();
                params.put("p_total_fee", price);
                params.put("p_token", token);
                params.put("p_custom", custom);

                params.put("p_promote_id", promote_id);
                params.put("p_good", goodName);
                window.showDialog("正在支付...")
                MyHTTPutils().httpPost(this@PayActivity, params, Contents.Acts.BONUS, object : MyHTTPutils.ResultListener {
                    override fun onResult(result: String) {
                        window.dissmissDialog()
                        try {
                            var jo = JSONObject(result)
                            var result_int = jo.getInt("result")

                            if (result_int == 1) {
                                payListener.onPayResult(true)
                                finish()


                            } else {
                                payListener.onPayResult(false)
                                finish()
                            }
                        } catch(e: JSONException) {
                            toast("数据异常")

                        }
                    }

                    override fun onError(result: String) {
                        window.dissmissDialog()
                        toast("网络异常")
                    }
                })

            }

            override fun onFalseListener(v: View) {
                window.dissmissWindow()
            }
        })
    }

    /**
     * 平台币支付
     */
    private fun platformPay() {
        var window = WindowUtils(this@PayActivity)
        window.showWindow("平台币支付", "平台币是由游酷官方补偿玩家的,您确定开始支付吗？", pay, object : MyWindowBtnListener {
            override fun onTrueListener(v: View) {
                window.dissmissWindow()
                var params = TreeMap<String, String>();
                params.put("p_total_fee", price);
                params.put("p_token", token);
                params.put("p_custom", custom);

                params.put("p_promote_id", promote_id);
                params.put("p_good", goodName);
                window.showDialog("正在支付...")
                MyHTTPutils().httpPost(this@PayActivity, params, Contents.Acts.CURRENCY, object : MyHTTPutils.ResultListener {
                    override fun onResult(result: String) {
                        window.dissmissDialog()
                        try {
                            var jo = JSONObject(result)
                            var result_int = jo.getInt("result")

                            if (result_int == 1) {
                                payListener.onPayResult(true)
                                finish()


                            } else {
                                payListener.onPayResult(false)
                                finish()
                            }
                        } catch(e: JSONException) {
                            toast("数据异常")

                        }
                    }

                    override fun onError(result: String) {
                        window.dissmissDialog()
                        toast("网络异常")
                    }
                })

            }

            override fun onFalseListener(v: View) {
                window.dissmissWindow()

            }
        })
    }


}
