package com.example.renessans7.utils

import android.content.Context
import androidx.core.content.edit
import com.example.renessans7.utils.Constants.SHARED_PREF
import com.example.renessans7.utils.Constants.TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext val context: Context) {

    private val pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    var token: String
        get() = pref.getString(TOKEN, "")!!
        set(value) = pref.edit { this.putString(TOKEN, value) }

}