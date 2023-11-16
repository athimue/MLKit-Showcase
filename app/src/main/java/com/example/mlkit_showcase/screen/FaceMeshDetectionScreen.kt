package com.example.mlkit_showcase.screen

import android.util.Log
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mlkit_showcase.analyser.FaceMeshDetectionAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.facemesh.FaceMesh

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceMeshDetectionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (cameraPermissionState.status.isGranted) {
            FaceMeshDetectionContent(onBackClick = onBackClick)
        } else {
            NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
        }
    }
}

@Composable
fun FaceMeshDetectionContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var faceMeshes by remember { mutableStateOf(listOf<FaceMesh>()) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    Column {
        Box(modifier = modifier.weight(0.6f)) {
            CameraView(modifier = modifier, imageAnalyser = FaceMeshDetectionAnalyser(
                onDetection = {
                    faceMeshes = it
                }
            ), onPreviewChanged = { previewView = it })
            FaceMeshOverlay(faceMeshes = faceMeshes)
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp), onClick = onBackClick
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun FaceMeshOverlay(faceMeshes: List<FaceMesh>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        faceMeshes.forEach { faceMesh ->
            val points = faceMesh.allPoints
            points.forEach { point ->
                val position = point.position
                drawCircle(
                    color = Color.Magenta,
                    radius = 2f,
                    center = Offset(position.x, position.y)
                )
            }
        }
    }
}
