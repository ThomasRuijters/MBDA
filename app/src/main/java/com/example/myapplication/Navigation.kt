package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Views.StartScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.StartScreen.route) {
            StartScreen(
                onNext = { navController.navigate(Screen.HomeScreen.route, ) },
                modifier = Modifier
            )
        }
        composable(route = Screen.HomeScreen.route) {
            Box {
                Text("Home Screen")
            }
        }
    }
}