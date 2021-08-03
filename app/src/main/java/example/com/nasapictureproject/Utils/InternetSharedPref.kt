package example.com.nasapictureproject.Utils

import android.content.Context
import android.content.SharedPreferences

class InternetSharedPref {
    private val sharedPrefFile = "INTERNETCHECK"
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var IS_ENABLE_INTERNET: String = "false"

    fun instancePref(context: Context) {
        sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setInternet(key: String) {
        editor.putString(IS_ENABLE_INTERNET, key)
        editor.apply()
        editor.commit()
    }

    fun getInternet(): String? {
        var sharedValue = sharedPreferences.getString(IS_ENABLE_INTERNET, IS_ENABLE_INTERNET)
        return sharedValue
    }

}