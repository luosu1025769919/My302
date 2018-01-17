package com.luosu.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 落苏 on 2017/10/26.
 */
data class User(
        var username: String = "",
        var user_id: String = "",
        var password: String = "",
        var password_type: String = "",
        var token: String = ""

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(username)
        writeString(user_id)
        writeString(password)
        writeString(password_type)
        writeString(token)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}