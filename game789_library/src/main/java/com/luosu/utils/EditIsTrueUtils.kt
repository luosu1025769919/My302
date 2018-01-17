package com.luosu.utils

import android.text.TextUtils
import android.widget.EditText

/**
 * Created by 落苏 on 2017/10/30.
 */
class EditIsReasonableUtils {
    companion object {
        fun nameIsTrue(edit: EditText): Boolean {
            var name: String = edit.text.toString()

            if (!TextUtils.isEmpty(name) && name.length >= 6) {
                return true
            }
            return false

        }

        fun passIsTrue(edit: EditText): Boolean {
            var name: String = edit.text.toString()
            String
            if (!TextUtils.isEmpty(name) && name.length >= 6 && name.matches(regex = Regex(".*[a-zA-Z]+.*"))) {
                return true
            }
            return false

        }
    }


}