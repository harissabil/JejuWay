package com.holidai.jejuway.data.supabase.auth

import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.shared_pref.UserSession
import com.holidai.jejuway.data.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseAuth(
    private val supabaseClient: SupabaseClient,
    private val userSession: UserSession,
) {
    suspend fun register(email: String, password: String, fullName: String): Resource<Boolean> {
        try {
            supabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put("full_name", fullName)
                }
            }
            saveUserSession()
            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
    }

    suspend fun login(email: String, password: String): Resource<Boolean> {
        try {
            supabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            saveUserSession()
            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
    }

    suspend fun logout(): Resource<Boolean> {
        try {
            supabaseClient.client.auth.signOut()
            clearUserSession()
            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
    }


    suspend fun isLoggedIn(): Resource<Boolean> {
        try {
            val token = getAccessToken()
            token?.let {
                supabaseClient.client.auth.retrieveUser(it)
                supabaseClient.client.auth.refreshCurrentSession()
                saveUserSession()
                return Resource.Success(true)
            }
            return Resource.Success(false)
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
    }

    suspend fun getUserId(): Resource<String> {
        try {
            val token = getAccessToken()
            token?.let {
                val user = supabaseClient.client.auth.retrieveUser(it)
                return Resource.Success(user.id)
            }
            return Resource.Error("User not found")
        } catch (e: Exception) {
            return Resource.Error(e.message)
        }
    }

    private fun saveUserSession() {
        userSession.saveUserSession(supabaseClient.client.auth.currentAccessTokenOrNull())
    }

    private fun clearUserSession() {
        userSession.saveUserSession(null)
    }

    private fun getAccessToken(): String? {
        return userSession.getUserSession()
    }
}