package com.ahmed.a.habib.deliverynow.dataStore.auth

import com.ahmed.a.habib.deliverynow.features.auth.User


interface AuthDataStore {
    
    suspend fun cashUser(user: User)

    suspend fun getCashedUser(): User
}