package com.ahmed.a.habib.deliverynow.features.auth.register

import com.ahmed.a.habib.deliverynow.features.auth.User

sealed class RegisterIntents {

    data class CashUser(val user: User) : RegisterIntents()

    data class ValidateAndRegister(val user: User) : RegisterIntents()
}