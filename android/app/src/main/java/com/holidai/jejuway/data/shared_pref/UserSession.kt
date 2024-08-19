package com.holidai.jejuway.data.shared_pref

import android.content.Context

class UserSession(private val context: Context) {

    companion object {
        private const val USER_SESSION = "user_session"
        private const val USER_TOKEN = "user_token"
    }

    fun saveUserSession(token: String?) {
        context.getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE).edit().apply {
            putString(USER_TOKEN, token)
            apply()
        }
    }

    fun getUserSession(): String? {
        return context.getSharedPreferences(USER_SESSION, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
    }
}