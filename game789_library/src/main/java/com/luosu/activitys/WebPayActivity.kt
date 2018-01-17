package com.luosu.activitys

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import android.widget.ImageView
import com.alipay.sdk.app.PayTask
import com.luosu.game789_library.R
import com.luosu.sdk.Game789Sdk
import com.luosu.utils.BaseUtils
import com.luosu.utils.UkLogUtil


class WebPayActivity : AppCompatActivity() {


    internal lateinit var mwebview: WebView
    internal lateinit var back: ImageView
    internal lateinit var pay_way: String
    internal lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_pay)
        mwebview = findViewById(R.id.uk_webview) as WebView
        back = findViewById(R.id.uk_back_webview) as ImageView

        useWebView()
        pay_way = intent.getStringExtra("pay_way")
        url = intent.getStringExtra("url")


        // 需要区分是支付宝web还是微信
        if (pay_way.equals("wxpay")) {
            mwebview.loadUrl(url)
        } else if (pay_way.equals("alipay")) {
            UkLogUtil.i("alipay", url)

            mwebview.loadDataWithBaseURL(null, url, "text/html", "utf-8", null)

        }
        back.setOnClickListener({
            Game789Sdk.payListener.onCancel()
            BaseUtils.finishActivity("payactivity")
            finish()
        })
    }

    private fun useWebView() {
        UkLogUtil.i("WeiXinWebViewActivity", "数据：-------")
        mwebview.setBackgroundColor(Color.parseColor("#00000000"))
        val settings = mwebview.getSettings()

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        // }

        settings.setDefaultTextEncodingName("UTF-8") // 设置默认的显示编码
        settings.setUserAgentString(settings.getUserAgentString())
        settings.setJavaScriptCanOpenWindowsAutomatically(true)// 设置js可以直接打开窗口，如window.open()，默认为false
        settings.setJavaScriptEnabled(true)
        settings.setSupportZoom(false)// 是否可以缩放，默认true
        settings.setBuiltInZoomControls(false)// 是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true)// 设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true)// 和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setPluginState(WebSettings.PluginState.ON)
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.setSupportMultipleWindows(true)
        settings.setJavaScriptEnabled(true)
        settings.setSavePassword(false)
        settings.setJavaScriptCanOpenWindowsAutomatically(true)
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8)
        settings.setAllowFileAccess(false)
        settings.setTextSize(WebSettings.TextSize.NORMAL)
        mwebview.addJavascriptInterface(JsInteration(), "vvpayJsInterface")
        mwebview.setWebChromeClient(WebChromeClient())
        mwebview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        requireNotNull(request,{})

                    var url= request?.url
                };
                return super.shouldOverrideUrlLoading(view, request)
            }


            override fun shouldOverrideUrlLoading(view: WebView, httpurl: String): Boolean {
                //调起微信
                if (httpurl.startsWith("weixin:") || httpurl.startsWith("weixin:")) {
                    val intent = Intent(ACTION_VIEW, Uri.parse(httpurl))
                    startActivity(intent)
                    return true
                }

                if (!(httpurl.startsWith("http") || httpurl.startsWith("https"))) {
                    return true
                }
//调起支付宝原端支付
                val task = PayTask(this@WebPayActivity)
                val ex = task.fetchOrderInfoFromH5PayUrl(httpurl)
                if (!TextUtils.isEmpty(ex)) {
                    Log.i("tag", "paytask:::::" + httpurl)
                    println("paytask:::::" + httpurl)
                    Thread(Runnable {
                        Log.i("tag", "paytask:::::" + ex)
                        println("payTask:::" + ex)
                        val result = task.h5Pay(ex, true)
                        if (!TextUtils.isEmpty(result.getReturnUrl())) {
                            this@WebPayActivity.runOnUiThread(java.lang.Runnable { mwebview.loadUrl(result.getReturnUrl()) })
                        }
                    }).start()
                } else {
                    Log.i("tag", "ex为空：" + httpurl)
                    view.loadUrl(httpurl)
                }
                return true

            }


        })


    }

    inner class JsInteration {

        /**
         * 2

         * @param resultcode
         * *            0为用户取消，1为成功,-1为失败
         */
        @JavascriptInterface
        fun vvpayResult(resultcode: Int, trade_no: String, vvpay_order_id: String) {
            UkLogUtil.i("tag", "结果调用")

            when (resultcode) {
                0 -> {
                    this@WebPayActivity.finish()
                    BaseUtils.finishActivity("payactivity")
                    Game789Sdk.payListener.onCancel()
                }

                1 -> {
                    this@WebPayActivity.finish()
                    BaseUtils.finishActivity("payactivity")
                    Game789Sdk.payListener.onPayResult(true)
                }

                -1 -> {
                    this@WebPayActivity.finish()
                    BaseUtils.finishActivity("payactivity")
                    Game789Sdk.payListener.onPayResult(false)
                }


            }


        }

        @JavascriptInterface
        fun weiXinPayResult(result: Boolean) {
            UkLogUtil.i("tag", "result:----" + result)
            if (result) {
            } else {

            }

        }

        /**
         * 已完成支付
         */
        @JavascriptInterface
        fun userFinishPay() {
            this@WebPayActivity.finish()
            BaseUtils.finishActivity("payactivity")
            Game789Sdk.payListener.onPayResult(true)

        }

        /**
         * 用户取消
         */
        @JavascriptInterface
        fun cancel() {

        }

    }
}
