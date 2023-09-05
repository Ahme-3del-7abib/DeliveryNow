package com.ahmed.a.habib.deliverynow.features.auth.register

import com.ahmed.a.habib.deliverynow.features.auth.User

sealed class RegisterViewStates {

    object Loading : RegisterViewStates()

    object UserIsCashed : RegisterViewStates()

    data class IsRegistered(val user: User) : RegisterViewStates()

    data class ResError(val res: Int) : RegisterViewStates()

    data class MsgError(val res: String) : RegisterViewStates()
}