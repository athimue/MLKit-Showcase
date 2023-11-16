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
                DrawSegmentationMask(it)
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
fun DrawSegmentationMask(mask: SegmentationMask) {
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(mask) {
        withContext(Dispatchers.IO) {
            val bitmap = Bitmap.createBitmap(mask.width, mask.height, Bitmap.Config.ARGB_8888)
            mask.buffer.rewind()

            for (y in 0 until mask.height) {
                for (x in 0 until mask.width) {
                    val pixelValue =
                        if (mask.buffer.get().toInt() == 1) Color.White else Color.Transparent
                    bitmap.setPixel(x, y, pixelValue.toArgb())
                }
            }

            imageBitmap.value = bitmap.asImageBitmap()
        }
    }

    imageBitmap.value?.let {
        Image(bitmap = it, contentDescription = "Segmentation Mask")
    }
}


