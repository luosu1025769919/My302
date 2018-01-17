package com.luosu.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.luosu.game789_library.R

class SimpleLoginActivity : AppCompatActivity() {


    internal lateinit var linear_pro: ImageView
    internal lateinit var progressbar_dialog_login: ProgressBar
    internal lateinit var third_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_login)
        initViews()
        third_id = intent.getStringExtra("third_id")
        if (TextUtils.isEmpty(third_id)) {
            linear_pro.visibility = View.VISIBLE
        } else {
            linear_pro.visibility = View.INVISIBLE
        }

        linear_pro.postDelayed({
            thirdLogin()

        }, 2000)

    }

    private fun thirdLogin() {

    }

    private fun initViews() {
        linear_pro = findViewById(R.id.linear_pro) as ImageView
        progressbar_dialog_login = findViewById(R.id.progressbar_dialog_login) as ProgressBar
        registerListener()
    }

    private fun registerListener() {
        linear_pro.setOnClickListener({


        })
    }


}
