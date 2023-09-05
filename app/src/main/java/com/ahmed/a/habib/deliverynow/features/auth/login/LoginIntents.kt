package com.ahmed.a.habib.deliverynow.features.auth.login

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.ahmed.a.habib.deliverynow.features.auth.User


sealed class LoginIntents {

    data class GetGoogleAccount(
        val activity: Activity,
        val callback: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) : LoginIntents()

    data class GetUserFromFireStoreAndCashIt(val user: User) : LoginIntents()

    data class ValidateAndLogin(val email: String, val password: String) : LoginIntents()

    data class SignInWithGoogle(val activityResult: ActivityResult) : LoginIntents()
}
