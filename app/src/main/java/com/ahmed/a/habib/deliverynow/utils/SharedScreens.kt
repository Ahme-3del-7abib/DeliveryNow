package com.ahmed.a.habib.deliverynow.utils

import com.ahmed.a.habib.deliverynow.utils.SharedKeys.Companion.splash
import com.ahmed.a.habib.deliverynow.utils.SharedKeys.Companion.login
import com.ahmed.a.habib.deliverynow.utils.SharedKeys.Companion.register
import com.ahmed.a.habib.deliverynow.utils.SharedKeys.Companion.usersDashboard

sealed class SharedScreens(val route: String) {

    object Splash : SharedScreens(splash)

    object Login : SharedScreens(login)

    object Register : SharedScreens(register)

    object UsersDashboard :SharedScreens(usersDashboard)
}
