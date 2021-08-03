package example.com.nasapictureproject.Utils

import android.content.Context
import android.content.SharedPreferences

class CurrentFragment {
    private val sharedPrefFile = "CURRENTFRAGMENT"
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var CURRENT_FRAGMENT: String = "false"

    fun instancePref(context: Context) {
        sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun setCurrentFragment(key: String) {
        editor.putString(CURRENT_FRAGMENT, key)
        editor.apply()
        editor.commit()
    }

    fun getCurrentFragment(): String? {
        var sharedValue = sharedPreferences.getString(CURRENT_FRAGMENT, CURRENT_FRAGMENT)
        return sharedValue
    }

}