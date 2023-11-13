package com.example.mlkit_showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkit_showcase.composable.HomeComposable
import com.example.mlkit_showcase.composable.TextRecognitionComposable

internal sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object TextRecognition : Screen("text_recognition")
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeComposable(onTextRecognitionClick = { navController.navigate(Screen.TextRecognition.route) })
        }
        composable(Screen.TextRecognition.route) {
            TextRecognitionComposable(onBackClick = { navController.popBackStack() })
        }
    }
}