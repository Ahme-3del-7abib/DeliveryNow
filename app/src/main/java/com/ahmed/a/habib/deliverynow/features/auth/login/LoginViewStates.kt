package com.ahmed.a.habib.deliverynow.features.auth.login

import com.ahmed.a.habib.deliverynow.features.auth.User


sealed class LoginViewStates {

    object Loading : LoginViewStates()

    object UserIsCashed : LoginViewStates()

    data class IsLoggedIn(val user: User) : LoginViewStates()

    data class ResError(val res: Int) : LoginViewStates()

    data class MsgError(val msg: String) : LoginViewStates()
}