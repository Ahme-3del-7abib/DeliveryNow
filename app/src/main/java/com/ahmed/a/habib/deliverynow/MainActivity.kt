package com.ahmed.a.habib.deliverynow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmed.a.habib.deliverynow.features.auth.login.LoginScreen
import com.ahmed.a.habib.deliverynow.features.auth.register.RegisterScreen
import com.ahmed.a.habib.deliverynow.features.splash.SplashScreen
import com.ahmed.a.habib.deliverynow.users.features.dashboard.DashboardContainerScreen
import com.ahmed.a.habib.deliverynow.utils.SharedScreens
import com.ahmed.a.habib.deliverynow.utils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = SharedScreens.Splash.route,
                ) {
                    composable(route = SharedScreens.Splash.route) {
                        SplashScreen(navController)
                        window.changeStatusBarColor(R.color.dark_yellow, this@MainActivity)
                    }

                    composable(route = SharedScreens.Login.route) {
                        LoginScreen(this@MainActivity, navController)
                        window.changeStatusBarColor(R.color.dark_yellow, this@MainActivity)
                    }

                    composable(route = SharedScreens.Register.route) {
                        RegisterScreen(this@MainActivity, navController)
                        window.changeStatusBarColor(R.color.navy_blue, this@MainActivity)
                    }

                    composable(route = SharedScreens.UsersDashboard.route) {
                        DashboardContainerScreen()
                        window.changeStatusBarColor(R.color.navy_blue, this@MainActivity)
                    }
                }
            }
        }
    }
}