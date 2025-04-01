package com.example.myapplication

sealed class Screen(val route: String) {
    object StartScreen : Screen("start_screen")
    object HomeScreen : Screen("home_screen")
}