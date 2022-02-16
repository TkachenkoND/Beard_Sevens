package ram.hesokio.srawber.domain

import android.content.Context
import android.content.SharedPreferences
import java.io.File
import java.io.FileInputStream

const val LINK = "link"
const val APSS_KEY = "appsLink"
const val APP_PREFERENCES = "mysettings"

class WorkWithSharedPref(context: Context) {

    private var shared: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    private fun saveFullLinkInShared() {
        val editor: SharedPreferences.Editor = shared.edit()

        editor.putString(LINK, "https://trident.website/tMsrTdBb")
        editor.apply()

        editor.putString(APSS_KEY, "JuCZJayajy82MSWXymXARW")
        editor.apply()
    }

    init {
        saveFullLinkInShared()
    }

    fun getLink() = shared.getString(LINK, "")

    fun getAppsKey() = shared.getString(APSS_KEY, "")

}