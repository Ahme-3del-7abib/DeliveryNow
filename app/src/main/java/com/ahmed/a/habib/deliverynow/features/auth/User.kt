package com.ahmed.a.habib.deliverynow.features.auth

data class User(
    val name: String,
    val email: String,
    var userType: String,
    var password: String,
    var id: String = "",
    val imgUrl: String = ""
)