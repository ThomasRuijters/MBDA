package com.example.myapplication

sealed class Screen(val route: String) {
    object StartScreen : Screen("start_screen")
    object StratagemListScreen : Screen("stratagem_list_screen")
    object stratagemScreen : Screen("stratagem_screen/{id}")
    object settingsScreen : Screen("settings_screen")
}