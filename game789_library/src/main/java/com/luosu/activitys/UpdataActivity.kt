package com.luosu.activitys

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.luosu.game789_library.R
import com.luosu.listener.MyWindowBtnListener
import com.luosu.sdk.Game789Sdk
import com.luosu.utils.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class UpdataActivity : AppCompatActivity() {
    internal lateinit var updata: FrameLayout
    internal lateinit var window: WindowUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updata)
        updata = findViewById(R.id.updata) as FrameLayout
        window = WindowUtils(this@UpdataActivity)
        updata.post(Runnable {
            updata()

        })


    }

    private lateinit var downloadUrl: String

    fun updata() {
        window.showDialog("检测更新")
        var params = TreeMap<String, String>()
        var nowUkVersion = BaseUtils.getMetaData(this@UpdataActivity, "789GAMESDK_VERSION_NUMBER")
        params.put("p_version_number", nowUkVersion)
        params.put("p_append", "");


        MyHTTPutils().httpPost(this@UpdataActivity, params, Contents.Acts.UPDATA, object : MyHTTPutils.ResultListener {
            override fun onResult(result: String) {
                window.dissmissDialog()
                try {
                    var jo = JSONObject(result)
                    var result_int = jo.getInt("result")
                    var msg = jo.getString("msg")
                    if (result_int == 1) {
                        val maxversionNumber = jo.getJSONObject("data").getInt("game_ver")
                        downloadUrl = jo.getJSONObject("data").getString("game_download")
                        // 是否是百推，1为百推，0为非百推
                        val is_baitui = jo.getJSONObject("data").getString("is_baitui")

                        PrefUtils(this@UpdataActivity, PrefUtils.USER_MESSAGES).setString(PrefUtils.IS_BAITUI, is_baitui)
                        if (Integer.parseInt(nowUkVersion) < maxversionNumber) {
                            window.showWindow("需要更新", "游戏需要更新了，请更新再继续游戏!", updata, object : MyWindowBtnListener {
                                override fun onTrueListener(v: View) {
                                    window.dissmissWindow()
                                    val netType = BaseUtils.GetNetype(this@UpdataActivity)
                                    if (downloadUrl != null)
                                        if (netType == 1) {
                                            this@UpdataActivity.startActivity(Intent(ACTION_VIEW, Uri.parse(downloadUrl)))
                                        } else {
                                            window.showWindow("非wifi状况", "在非wifi状况下您决定继续下载吗？", updata, object : MyWindowBtnListener {
                                                override fun onTrueListener(v: View) {
                                                    window.dissmissWindow()
                                                    this@UpdataActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl)))
                                                }

                                                override fun onFalseListener(v: View) {
                                                    window.dissmissWindow()
                                                    Game789Sdk.updataListener.onCancel()
                                                    finish()
                                                }

                                            })
                                        }


                                }

                                override fun onFalseListener(v: View) {
                                    window.dissmissWindow()
                                    Game789Sdk.updataListener.onCancel()
                                    finish()
                                }

                            })

                        } else {
                            // 不需要更新的时候调用的:


                            finish();
                        }

                    } else {
                        toast(msg)
                        lodingError()
                    }
                } catch(e: JSONException) {
                    lodingError()
                }


            }


            override fun onError(result: String) {
                window.dissmissDialog()
                lodingError()
            }

        })
    }

    private fun lodingError() {

        window.showWindow("更新错误", "更新检测过程发生错误，您是否重新检测更新？", updata, object : MyWindowBtnListener {
            override fun onTrueListener(v: View) {
                window.dissmissWindow()
                updata()
            }

            override fun onFalseListener(v: View) {
                window.dissmissWindow()
                Game789Sdk.updataListener.onCancel()
                finish()
            }


        })
    }

    fun Activity.toast(msg: String) {
        Toast.makeText(this@UpdataActivity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Game789Sdk.updataListener.onCancel()
    }

}