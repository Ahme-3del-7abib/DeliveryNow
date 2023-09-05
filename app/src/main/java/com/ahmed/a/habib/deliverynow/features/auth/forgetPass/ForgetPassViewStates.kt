package com.ahmed.a.habib.deliverynow.features.auth.forgetPass

sealed class ForgetPassViewStates {

    object Loading : ForgetPassViewStates()

    data class MailIsSent(val email: String) : ForgetPassViewStates()

    data class ShowMsgError(val msg: String) : ForgetPassViewStates()

    data class ShowResError(val res: Int) : ForgetPassViewStates()
}