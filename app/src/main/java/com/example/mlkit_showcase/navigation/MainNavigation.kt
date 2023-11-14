package com.example.mlkit_showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkit_showcase.screen.BarcodeScanningScreen
import com.example.mlkit_showcase.screen.HomeScreen
import com.example.mlkit_showcase.screen.ImageLabelingScreen
import com.example.mlkit_showcase.screen.ObjectDetectionScreen
import com.example.mlkit_showcase.screen.TextRecognitionScreen

internal sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object TextRecognition : Screen("text_recognition")
    data object ObjectDetection : Screen("object_detection")
    data object ImageLabeling : Screen("image_labeling")
    data object BarcodeScanning : Screen("barcode_scanning")
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onTextRecognitionClick = { navController.navigate(Screen.TextRecognition.route) },
                onObjectDetectionClick = { navController.navigate(Screen.ObjectDetection.route) },
                onImageLabelingClick = { navController.navigate(Screen.ImageLabeling.route) },
                onBarcodeScanningClick = { navController.navigate(Screen.BarcodeScanning.route) },
            )
        }
        composable(Screen.TextRecognition.route) {
            TextRecognitionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.ObjectDetection.route) {
            ObjectDetectionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.ImageLabeling.route) {
            ImageLabelingScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.BarcodeScanning.route) {
            BarcodeScanningScreen(onBackClick = { navController.popBackStack() })
        }
    }
}