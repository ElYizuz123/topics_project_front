package com.example.healtyapp.util
import android.content.Context
import android.content.Context.MODE_PRIVATE
object TokenStore {
    private const val PREFS = "healty_prefs"
    private const val KEY_ACCESS = "access_token"
    private const val KEY_REFRESH = "refresh_token"
    fun save(context: Context, access: String, refresh: String) {
        context.getSharedPreferences(PREFS, MODE_PRIVATE)
            .edit()
            .putString(KEY_ACCESS, access)
            .putString(KEY_REFRESH, refresh)
            .apply()
    }
    fun getAccess(context: Context): String? =
        context.getSharedPreferences(PREFS, MODE_PRIVATE)
            .getString(KEY_ACCESS, null)
}