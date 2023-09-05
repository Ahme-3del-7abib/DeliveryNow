package com.ahmed.a.habib.deliverynow.users.utils

import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.users.utils.UsersKeys.Companion.homeScreen
import com.ahmed.a.habib.deliverynow.users.utils.UsersKeys.Companion.profileScreen
import com.ahmed.a.habib.deliverynow.users.utils.UsersKeys.Companion.searchScreen
import com.ahmed.a.habib.deliverynow.users.utils.UsersKeys.Companion.settingsScreen

sealed class UsersScreens(
    val route: String,
    val title: Int,
    val icon: Int,
    val icon_focused: Int
) {
    object Home : UsersScreens(
        route = homeScreen,
        title = R.string.home,
        icon = R.drawable.ic_bottom_home,
        icon_focused = R.drawable.ic_bottom_home_focused
    )

    object Search : UsersScreens(
        route = searchScreen,
        title = R.string.search,
        icon = R.drawable.search,
        icon_focused = R.drawable.focus_search
    )

    object Profile : UsersScreens(
        route = profileScreen,
        title = R.string.profile,
        icon = R.drawable.ic_bottom_profile,
        icon_focused = R.drawable.ic_bottom_profile_focused
    )

    object Settings : UsersScreens(
        route = settingsScreen,
        title = R.string.settings,
        icon = R.drawable.baseline_settings_24,
        icon_focused = R.drawable.baseline_settings_24
    )
}
