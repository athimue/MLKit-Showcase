package com.example.mlkit_showcase.screen

import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.mlkit_showcase.analyser.FaceDetectionAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.face.Face

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceDetectionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (cameraPermissionState.status.isGranted) {
            FaceDetectionContent(onBackClick = onBackClick)
        } else {
            NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
        }
    }
}

@Composable
fun FaceDetectionContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var faces by remember { mutableStateOf(listOf<Face>()) }
    var imageP by remember { mutableStateOf<ImageProxy?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    Column {
        Box(modifier = modifier.weight(0.6f)) {
            CameraView(modifier = modifier,
                imageAnalyser = FaceDetectionAnalyser { image, faceList ->
                    faces = faceList
                    imageP = image
                },
                onPreviewChanged = { previewView = it })
            imageP?.let {
                faces.firstOrNull()?.boundingBox?.let { points ->

                    val composeRect = androidx.compose.ui.geometry.Rect(
                        left = points.left.toFloat(),
                        top = points.top.toFloat(),
                        right = points.right.toFloat(),
                        bottom = points.bottom.toFloat()
                    )

                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawRect(
                            color = Color.Magenta,
                            topLeft = composeRect.topLeft,
                            size = composeRect.size,
                            style = Stroke(width = 3f)
                        )
                    }
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