package com.luosu.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Environment.getExternalStorageDirectory
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import org.xutils.DbManager
import org.xutils.x
import java.io.File
import java.io.IOException
import java.util.*
import java.util.zip.ZipFile


/**
 * Created by 落苏 on 2017/11/1.
 */

object BaseUtils {

    val activitys = HashMap<String, AppCompatActivity>()
    fun addActivity(name: String, activity: AppCompatActivity) {
        activitys.put(name, activity)
    };
    fun finishActivity(name: String) {
        val activity = activitys.get(name)
        if (activity != null) {
            activity!!.finish()
        }
    };

    fun getIMEI(context: Context): String {

        return (context.getSystemService(

                Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId

    }

    fun getMetaData(context: Context, name: String): String {
        try {
            var appinfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA);
            return appinfo.metaData.get(name).toString()
        } catch(e: PackageManager.NameNotFoundException) {
            return ""
        }


    }

    /**
     * Role:获取当前设置的电话号码 <BR></BR>
     * Date:2012-3-12 <BR></BR>

     * @author CODYY)peijiangping
     */
    fun getNativePhoneNumber(context: Context): String {
        var NativePhoneNumber: String? = null
        NativePhoneNumber = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number


        return NativePhoneNumber
    }

    fun getChannel(context: Context): String {
        val appinfo = context.applicationInfo
        val sourceDir = appinfo.sourceDir
        var ret = ""
        var zipfile: ZipFile? = null
        try {
            zipfile = ZipFile(sourceDir)
            val entries = zipfile.entries()
            while (entries.hasMoreElements()) {
                val entryName = entries.nextElement().name
                if (entryName.contains("s789channel")) {
                    ret = entryName
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        val split = ret.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split != null && split.size >= 2) {
            return ret.substring(split[0].length + 1)

        } else {
            return "0"
        }
    }

    /**
     * 本手机是否安装了微信

     * @param context
     * *
     * @return
     */
    fun weiXinIsInstall(context: Context): Boolean {
        // Intent i =
        // context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        // if (i == null) {
        // return false;
        // } else {
        // return true;
        // }
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo("com.tencent.mm", 0)
        } catch (e: PackageManager.NameNotFoundException) {
            packageInfo = null
            e.printStackTrace()
        }

        if (packageInfo == null) {
            return false
        } else {
            return true
        }
    }

    /**

     * @param context
     * *            上下文
     * *
     * @return 返回值 -1：没有网络 1：WIFI网络2：wap网络3：net网络
     */
    internal fun GetNetype(context: Context): Int {
        var netType = -1
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getActiveNetworkInfo() ?: return netType
        val nType = networkInfo.getType()
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3
            } else {
                netType = 2
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1
        }
        return netType
    }


    fun CreateDbUtils(context: Context, dbName: String) {
        val myname = "ukgame"
        val file1 = File(getExternalStorageDirectory().getAbsoluteFile(), "/android/data/" + myname + "/" + context.packageName)
        file1.mkdirs()
        var config: DbManager.DaoConfig = DbManager.DaoConfig();
        config.dbName = dbName
        config.dbDir = file1
        x.getDb(config)

    }


}
