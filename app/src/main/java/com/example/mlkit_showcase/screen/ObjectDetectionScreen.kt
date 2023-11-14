package com.example.mlkit_showcase.screen

import android.Manifest
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
import com.example.mlkit_showcase.analyser.ObjectRecognitionAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.example.mlkit_showcase.composable.TableCell
import com.example.mlkit_showcase.composable.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.objects.DetectedObject

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ObjectDetectionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    Scaffold(topBar = {
        TopBar(onBackClick = onBackClick, text = "Objects Detection")
    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (cameraPermissionState.status.isGranted) {
                ObjectDetectionContent()
            } else {
                NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
            }
        }
    })
}

@Composable
fun ObjectDetectionContent(
    modifier: Modifier = Modifier
) {
    var detectedObjects by remember { mutableStateOf(listOf<DetectedObject>()) }
    Column {
        CameraView(modifier = modifier.weight(1f),
            imageAnalyser = ObjectRecognitionAnalyser { detectedObjects = it })
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
                text = "Objects detected",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Divider()
            if (detectedObjects.isNotEmpty()) {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(text = "Id", weight = 0.2f)
                            TableCell(text = "Object", weight = 0.5f)
                            TableCell(text = "Confidence", weight = 0.3f)
                        }
                    }
                    items(items = detectedObjects) { detectedObject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(text = detectedObject.trackingId.toString(), weight = 0.2f)
                            TableCell(
                                text = detectedObject.labels.joinToString(", ") { label -> label.text },
                                weight = 0.5f
                            )
                            TableCell(
                                text = detectedObject.labels.joinToString(", ") { label -> label.confidence.toString() },
                                weight = 0.3f
                            )
                        }
                    }
                }
            } else {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    text = "No object detected",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}