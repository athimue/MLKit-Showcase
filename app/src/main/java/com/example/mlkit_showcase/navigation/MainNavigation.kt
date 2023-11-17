package com.example.mlkit_showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkit_showcase.screen.BarcodeScanningScreen
import com.example.mlkit_showcase.screen.DigitalInkRecognitionScreen
import com.example.mlkit_showcase.screen.FaceDetectionScreen
import com.example.mlkit_showcase.screen.FaceMeshDetectionScreen
import com.example.mlkit_showcase.screen.HomeScreen
import com.example.mlkit_showcase.screen.ImageLabelingScreen
import com.example.mlkit_showcase.screen.ObjectDetectionScreen
import com.example.mlkit_showcase.screen.PoseDetectionScreen
import com.example.mlkit_showcase.screen.SelfieSegmentationScreen
import com.example.mlkit_showcase.screen.SubjectSegmentationScreen
import com.example.mlkit_showcase.screen.TextRecognitionScreen

internal sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object TextRecognition : Screen("text_recognition")
    data object ObjectDetection : Screen("object_detection")
    data object ImageLabeling : Screen("image_labeling")
    data object BarcodeScanning : Screen("barcode_scanning")
    data object FaceDetection : Screen("face_detection")
    data object FaceMeshDetection : Screen("face_mesh_detection")
    data object DigitalInkRecognition : Screen("digital_ink_recognition")
    data object PoseDetection : Screen("pose_detection")
    data object SelfieSegmentation : Screen("selfie_segmentation")
    data object SubjectSegmentation : Screen("subject_segmentation")
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
                onFaceDetectionClick = { navController.navigate(Screen.FaceDetection.route) },
                onFaceMeshDetectionClick = { navController.navigate(Screen.FaceMeshDetection.route) },
                onDigitalInkRecognitionClick = { navController.navigate(Screen.DigitalInkRecognition.route) },
                onPoseDetectionClick = { navController.navigate(Screen.PoseDetection.route) },
                onSelfieSegmentationClick = { navController.navigate(Screen.SelfieSegmentation.route) },
                onSubjectSegmentationClick = { navController.navigate(Screen.SubjectSegmentation.route) },
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
        composable(Screen.FaceDetection.route) {
            FaceDetectionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.FaceMeshDetection.route) {
            FaceMeshDetectionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.DigitalInkRecognition.route) {
            DigitalInkRecognitionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.PoseDetection.route) {
            PoseDetectionScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.SelfieSegmentation.route) {
            SelfieSegmentationScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.SubjectSegmentation.route) {
            SubjectSegmentationScreen(onBackClick = { navController.popBackStack() })
        }
    }
}