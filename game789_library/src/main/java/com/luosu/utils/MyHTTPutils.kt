package com.luosu.utils


import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.luosu.listener.UpdataListener
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * Created by 落苏 on 2017/10/31.
 */
class MyHTTPutils {
    companion object {
        public val base_url = "https://sdkapi.mobile.x1616.com:8000/v8.html"
    }

    private var listener: ResultListener? = null
    private var imagelistener: ImageResultListener? = null

    interface ResultListener {
        fun onResult(result: String)

        fun onError(result: String)
    }

    interface ImageResultListener {


        fun onImgResult(result: Bitmap)

        fun onError(result: String)
    }

    fun httpget(url: String, listener: ResultListener?) {
        this.listener = listener

        object : AsyncTask<String, Int, String>() {

            override fun doInBackground(vararg params: String): String {
                val result: String
                Log.d("exception", "错误：url:---" + params[0])
                try {

                    val conn = URL(params[0]).openConnection() as HttpURLConnection
                    conn.setRequestMethod("GET")
                    conn.connect()

                    val code = conn.getResponseCode()
                    Log.d("exception", "错误：code" + code)

                    if (code == 200) {
                        val br = BufferedReader(InputStreamReader(conn.getInputStream()))
                        result = br.readLine()
                        br.close()
                        Log.d("exception", "错误：result" + result)
                        return result

                    } else {
                        return "error" + code
                    }
                } catch (e: MalformedURLException) {
                    Log.d("exception", "错误：MalformedURLException")
                    e.printStackTrace()
                } catch (e: IOException) {
                    Log.d("exception", "错误：IOException")
                    e.printStackTrace()
                }

                return "error"
            }

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun onPostExecute(result: String) {
                Log.d("exception", "错误：result" + result)
                //                super.onPostExecute(result);
                if (listener != null) {
                    if (!result.contains("error")) {
                        listener.onResult(result)
                    } else {
                        listener.onError(result)
                    }

                }
            }

            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
            }

        }.execute(base_url)


    }


    /**
     * post请求，

     * @param map      参数集合
     * *
     * @param act      动作名字
     * *
     * @param listener 监听回调
     */
    fun httpPost(context: Context, map: TreeMap<String, String>, act: String, listener: ResultListener) {
        this.listener = listener


        object : AsyncTask<String, Int, String>() {

            override fun doInBackground(vararg params: String): String {
                val result: String
                Log.d("exception", "错误：url:---" + params[0])
                try {

                    val conn = URL(params[0]).openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.doInput = true
                    conn.doOutput = true
                    conn.connectTimeout = 5000
                    val writer = PrintWriter(conn.outputStream, true)

                    val postmessage = "p_act=" + act + disposeMap(context, map, act)

                    writer.write(postmessage)
                    writer.flush()
                    val code = conn.responseCode
                    Log.d("exception", "错误：code" + code)

                    if (code == 200) {
                        val br = BufferedReader(InputStreamReader(conn.inputStream))
                        result = br.readLine()
                        br.close()
                        Log.d("exception", "错误：result" + result)
                        return result

                    } else {
                        return "error" + code
                    }
                } catch (e: MalformedURLException) {
                    Log.d("exception", "错误：MalformedURLException")
                    e.printStackTrace()
                } catch (e: IOException) {
                    Log.d("exception", "错误：IOException")
                    e.printStackTrace()
                }

                return "error"
            }

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun onPostExecute(result: String) {
                Log.d("exception", "错误：result" + result)
                //                super.onPostExecute(result);
                if (!result.contains("error")) {
                    listener.onResult(result)
                } else {
                    listener.onError(result)
                }
            }

            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
            }

        }.execute(base_url)


    }

    private fun disposeMap(context: Context, map: TreeMap<String, String>?, act: String): String {
        var prefutils: PrefUtils = PrefUtils(context, PrefUtils.USER_MESSAGES)
        if (map != null) {
            map!!.put("p_client_type", "0")
            map!!.put("p_game_id", Contents.GameMessage.GAMESDK_APPID)
            map!!.put("p_sid", prefutils.getString(PrefUtils.SID,"1"))
            map!!.put("p_time", System.currentTimeMillis().toString() + "")
            map!!.put("p_imei", BaseUtils.getIMEI(context))
            map!!.put("p_phonenumber", BaseUtils.getNativePhoneNumber(context))

            map!!.put("p_cid", BaseUtils.getChannel(context))
            map!!.put("p_sdk_ver", Contents.GameMessage.SDK_VER);
            map!!.put("p_phoneinfo", (android.os.Build.MODEL + "_" + android.os.Build.MANUFACTURER).replace(" ", ""))
            val sbuider: StringBuilder = StringBuilder()
            val sbuiderfosign = StringBuilder()
            sbuiderfosign.append(act.trim { it <= ' ' })
            sbuiderfosign.append("|")
            for (key in map.keys) {
                sbuider.append("&" + key + "=" + map!!.get(key))
                sbuiderfosign.append(map!!.get(key)?.trim())
                sbuiderfosign.append("|")
            }
            sbuiderfosign.append("QnxzMmVJZAC6XYmz");
            sbuiderfosign.append("|");
            sbuiderfosign.append(Contents.GameMessage.GAMESDK_APPKEY)

            sbuider.append("&sign=" + stringToMD5(sbuiderfosign.toString()))
            return sbuider.toString().trim { it <= ' ' }
        }
        return ""
    }


    /**
     * 将字符串转成MD5值

     * @param string
     * *
     * @return
     */
    fun stringToMD5(string: String): String? {
        val hash: ByteArray

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {

            if (b.toInt() and 0xff < 0x10)
                hex.append("0")
            hex.append(Integer.toHexString( b.toInt() and 0xFF ))
        }

        return hex.toString()
    }
}