package com.jerny.moiz.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserDataStore {
    private val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES")

    suspend fun setUserId(context: Context, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun setUserToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = token
        }
    }

    suspend fun setUserName(context: Context, nickName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NICKNAME] = nickName
        }
    }

    suspend fun setJoinCode(context: Context, code: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.JOIN_CODE] = code
        }
    }

    fun getUserId(context: Context): Flow<String> =
        context.dataStore.data.map { it -> it[PreferencesKeys.USER_ID] ?: "" }

    fun getUserToken(context: Context): Flow<String> =
        context.dataStore.data.map { it -> it[PreferencesKeys.USER_TOKEN] ?: "" }

    fun getUserName(context: Context): Flow<String> =
        context.dataStore.data.map { it -> it[PreferencesKeys.USER_NICKNAME] ?: "" }

    fun getJoinCode(context: Context): Flow<String> =
        context.dataStore.data.map { it -> it[PreferencesKeys.JOIN_CODE] ?: "" }
}

object PreferencesKeys {
    val USER_ID = stringPreferencesKey("userId")
    val USER_TOKEN = stringPreferencesKey("token")
    val USER_NICKNAME = stringPreferencesKey("nickName")
    val JOIN_CODE = stringPreferencesKey("code")
}