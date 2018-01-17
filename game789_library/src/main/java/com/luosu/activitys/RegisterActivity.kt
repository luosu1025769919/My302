package com.luosu.activitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.luosu.bean.User
import com.luosu.game789_library.R
import com.luosu.utils.Contents
import com.luosu.utils.EditIsReasonableUtils
import com.luosu.utils.MyHTTPutils
import com.luosu.utils.UkLogUtil
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RegisterActivity : AppCompatActivity() {
    internal lateinit var edit_user: EditText
    internal lateinit var edit_pass: EditText

    internal lateinit var register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews()
    }

    private fun initViews() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        edit_user = findViewById(R.id.edit_user) as EditText
        edit_pass = findViewById(R.id.edit_pass) as EditText

        register = findViewById(R.id.register_btn) as TextView
        edit_user.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        registerListener()

    }

    fun registerListener() {
        register.setOnClickListener {
            UkLogUtil.i("点击了注册")
            register()


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Contents.ActivityJumpWhats.LOGIN_JUMP_REGISTER -> {

            }

        }
    }

    /**
     * 登录逻辑
     */
    fun register() {
        var user_name = edit_user.text.toString()
        var user_pass = edit_pass.text.toString()
        if (EditIsReasonableUtils.nameIsTrue(edit_user) && EditIsReasonableUtils.passIsTrue(edit_pass)) {
//            toast("用户名和密码正确")

            var httpUtils: MyHTTPutils = MyHTTPutils();
            var map: TreeMap<String, String> = TreeMap <String, String>();
            map.put("p_username", user_name);
            map.put("p_password", httpUtils.stringToMD5(user_pass)!!);
            map.put("p_password_type", "0");
            httpUtils.httpPost(this, map, Contents.Acts.USER_REG, object : MyHTTPutils.ResultListener {
                override fun onResult(result: String) {
                    toast(result)
                    UkLogUtil.i("注册结果:" + result)
                    try {
                        var jo: JSONObject = JSONObject(result)
                        var result_code = jo.getInt("result")
                        var msg = jo.getString("msg")

                        if (result_code == 1) {
                            var data = jo.getJSONObject("data")
                            var user: User = User()
                            user.username = data.getString("username")
                            user.password = data.getString("password")
                            user.user_id = data.getString("user_id")
                            user.password_type = data.getString("password_type")
                            user.token = data.getString("token")

                            var intent: Intent = Intent()
                            intent.putExtra("user", user)
                            setResult(Activity.RESULT_OK, intent)
                            finish()

                        } else {

                            toast(msg)
                        }
                    } catch(e: JSONException) {
                        toast("数据出错")

                    }

                }

                override fun onError(result: String) {
                    toast(result)
                    UkLogUtil.i("注册结果:" + result)
                }

            })


        } else {

            toast("用户名和密码不合法")
        }


    }

    fun Activity.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
