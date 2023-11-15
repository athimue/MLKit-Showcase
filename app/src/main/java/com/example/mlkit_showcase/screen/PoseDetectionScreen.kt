package com.example.mlkit_showcase.screen

import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mlkit_showcase.analyser.PoseDetectionAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PoseDetectionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (cameraPermissionState.status.isGranted) {
            PoseDetectionContent(onBackClick = onBackClick)
        } else {
            NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
        }
    }
}

@Composable
fun PoseDetectionContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var imageP by remember { mutableStateOf<ImageProxy?>(null) }
    var poseDetected by remember { mutableStateOf<Pose?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    Column {
        Box {
            CameraView(
                modifier = modifier,
                imageAnalyser = PoseDetectionAnalyser { image, pose ->
                    poseDetected = pose
                    imageP = image
                },
                onPreviewChanged = { previewView = it }
            )
            imageP?.let { img ->
                poseDetected?.let {
                    DrawPoseLandmarks(it.allPoseLandmarks)
                }
            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                onClick = onBackClick
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun BoxScope.DrawPoseLandmarks(landmarks: List<PoseLandmark>) {
    Canvas(modifier = Modifier.matchParentSize()) {
        landmarks.forEach { landmark ->
            val point = landmark.position
            drawCircle(
                color = Color.Red,
                radius = 10f,
                center = Offset(point.x, point.y)
            )
        }
    }
}
