package com.example.mlkit_showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkit_showcase.composable.HomeComposable

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeComposable()
        }
    }
}