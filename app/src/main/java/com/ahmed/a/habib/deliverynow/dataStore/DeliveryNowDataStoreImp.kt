package com.ahmed.a.habib.deliverynow.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.ahmed.a.habib.deliverynow.features.auth.User
import com.ahmed.a.habib.deliverynow.dataStore.auth.AuthDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull

class DeliveryNowDataStoreImp(private val context: Context) : AuthDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "delivery_now_datastore")

    override suspend fun cashUser(user: User) {
        context.dataStore.edit {
            it[DataStoreKeys.userKey] = Gson().toJson(user)
        }
    }

    override suspend fun getCashedUser(): User {
        val preferences = context.dataStore.data.firstOrNull()
        val jsonString = preferences?.get(DataStoreKeys.userKey)
        return Gson().fromJson(jsonString, User::class.java)
    }
}