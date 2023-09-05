package com.ahmed.a.habib.deliverynow.users.features.dashboard.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {
        Text(text = "Profile Screen", fontSize = 20.sp)
    }
}