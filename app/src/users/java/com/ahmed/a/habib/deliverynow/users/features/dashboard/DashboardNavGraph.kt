package com.ahmed.a.habib.deliverynow.users.features.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahmed.a.habib.deliverynow.users.features.dashboard.home.HomeScreen
import com.ahmed.a.habib.deliverynow.users.features.dashboard.profile.ProfileScreen
import com.ahmed.a.habib.deliverynow.users.features.dashboard.search.SearchScreen
import com.ahmed.a.habib.deliverynow.users.features.dashboard.settings.SettingsScreen
import com.ahmed.a.habib.deliverynow.users.utils.UsersScreens

@Composable
fun DashboardNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = UsersScreens.Home.route
    ) {
        composable(route = UsersScreens.Home.route) {
            HomeScreen()
        }

        composable(route = UsersScreens.Search.route) {
            SearchScreen()
        }

        composable(route = UsersScreens.Profile.route) {
            ProfileScreen()
        }

        composable(route = UsersScreens.Settings.route) {
            SettingsScreen()
        }
    }
}