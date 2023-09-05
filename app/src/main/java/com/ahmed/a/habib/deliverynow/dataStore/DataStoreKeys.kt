package com.ahmed.a.habib.deliverynow.dataStore

import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreKeys {
    companion object {
        val userKey = stringPreferencesKey("USER_KEY")
    }
}