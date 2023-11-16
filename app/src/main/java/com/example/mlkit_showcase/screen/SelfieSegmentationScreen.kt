package com.example.mlkit_showcase.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.mlkit_showcase.analyser.SelfieSegmentationAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.segmentation.SegmentationMask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelfieSegmentationScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (cameraPermissionState.status.isGranted) {
            SelfieSegmentationContent(onBackClick = onBackClick)
        } else {
            NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
        }
    }
}

@Composable
fun SelfieSegmentationContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var segmentationMask by remember { mutableStateOf<SegmentationMask?>(null) }
    Column {
        Box(modifier = modifier.weight(0.6f)) {
            CameraView(modifier = modifier,
                imageAnalyser = SelfieSegmentationAnalyser {
                    segmentationMask = it
                })
            segmentationMask?.let {
                SegmentationMaskOverlay(it)
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
fun SegmentationMaskOverlay(segmentationMask: SegmentationMask) {
    val maskBitmap = remember(segmentationMask) { segmentationMask.toBitmap() }
    Image(bitmap = maskBitmap.asImageBitmap(), contentDescription = null)
}

fun SegmentationMask.toBitmap(): Bitmap {
    val maskBuffer = this.buffer
    val maskWidth = this.width
    val maskHeight = this.height
    val bitmap = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888)
    for (y in 0 until maskHeight) {
        for (x in 0 until maskWidth) {
            val foregroundConfidence = maskBuffer.float
            val pixelValue = (foregroundConfidence * 255).toInt()
            val color =
                Color(red = pixelValue, green = pixelValue, blue = pixelValue, alpha = 255).toArgb()
            bitmap.setPixel(x, y, color)
        }
    }
    return bitmap
}
