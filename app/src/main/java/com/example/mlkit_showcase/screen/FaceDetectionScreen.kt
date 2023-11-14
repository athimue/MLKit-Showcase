package com.example.mlkit_showcase.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mlkit_showcase.analyser.FaceDetectionAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.example.mlkit_showcase.composable.TableCell
import com.example.mlkit_showcase.composable.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.face.Face

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FaceDetectionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Scaffold(topBar = {
        TopBar(onBackClick = onBackClick, text = "Face detection")
    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (cameraPermissionState.status.isGranted) {
                FaceDetectionContent()
            } else {
                NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
            }
        }
    })
}

@Composable
fun FaceDetectionContent(
    modifier: Modifier = Modifier
) {
    var faces by remember { mutableStateOf(listOf<Face>()) }
    Column {
        CameraView(
            modifier = modifier.weight(1f),
            imageAnalyser = FaceDetectionAnalyser { faces = it })
        Column(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = "Face detected",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Divider()
            if (faces.isNotEmpty()) {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(text = "Index", weight = 0.2f)
                            TableCell(text = "Smiling ?", weight = 0.5f)
                            TableCell(text = "Eyes open ?", weight = 0.3f)
                        }
                    }
                    items(items = faces) { face ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(text = face.trackingId.toString(), weight = 0.2f)
                            TableCell(
                                text = String.format("%.2f", face.smilingProbability.toString()),
                                weight = 0.5f
                            )
                            TableCell(
                                text = String.format(
                                    "%.2f", face.rightEyeOpenProbability.toString()
                                ), weight = 0.3f
                            )
                        }
                    }
                }
            } else {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    text = "No face",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}