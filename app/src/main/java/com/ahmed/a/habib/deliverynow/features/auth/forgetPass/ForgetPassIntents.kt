package com.ahmed.a.habib.deliverynow.features.auth.forgetPass

sealed class ForgetPassIntents {
    data class SendMail(val email: String) : ForgetPassIntents()
}