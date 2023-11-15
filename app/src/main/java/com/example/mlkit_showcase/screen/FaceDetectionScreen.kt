package com.example.mlkit_showcase.screen

import android.graphics.Rect
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import java.math.RoundingMode

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
    var imageP by remember { mutableStateOf<ImageProxy?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    Column {
        Box(modifier = modifier.weight(0.6f)) {
            CameraView(
                modifier = modifier,
                imageAnalyser = FaceDetectionAnalyser { image, faceList ->
                    faces = faceList
                    imageP = image
                },
                onPreviewChanged = { previewView = it }
            )
            imageP?.let { img ->
                faces.firstOrNull()?.boundingBox?.let { points ->

                    val imageAspectRatio = img.width.toFloat() / img.height
                    val previewAspectRatio =
                        previewView?.width?.toFloat()!! / previewView?.height?.toFloat()!!

                    val scale = if (imageAspectRatio < previewAspectRatio) {
                        previewView?.height?.toFloat()!! / img.height
                    } else {
                        previewView?.width?.toFloat()!! / img.width
                    }
                    val offsetX = 150

                    val transformedRect = Rect(
                        (points.left * scale + offsetX).toInt(),
                        (points.top * scale).toInt(),
                        (points.right * scale + offsetX).toInt(),
                        (points.bottom * scale).toInt()
                    )

                    val composeRect = androidx.compose.ui.geometry.Rect(
                        left = transformedRect.left.toFloat(),
                        top = transformedRect.top.toFloat(),
                        right = transformedRect.right.toFloat(),
                        bottom = transformedRect.bottom.toFloat()
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
        }
        Column(
            modifier = modifier
                .weight(0.4f)
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
                            TableCell(text = "Smiling ?", weight = 0.5f)
                            TableCell(text = "Eyes open ?", weight = 0.5f)
                        }
                    }
                    items(items = faces) { face ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(
                                text = face.smilingProbability?.toBigDecimal()
                                    ?.setScale(1, RoundingMode.UP).toString(), weight = 0.5f
                            )
                            TableCell(
                                text = face.rightEyeOpenProbability?.toBigDecimal()
                                    ?.setScale(1, RoundingMode.UP).toString(), weight = 0.5f
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