package com.example.mlkit_showcase.composable

import android.graphics.Rect
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

@Composable
fun CameraView(
    modifier: Modifier,
    imageAnalyser: ImageAnalysis.Analyzer,
    onPreviewChanged: (PreviewView) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = cameraProviderFuture.get()
    val executor = ContextCompat.getMainExecutor(context)
    val cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val preview = remember { mutableStateOf<Preview?>(null) }
    val imageAnalysis = remember { mutableStateOf<ImageAnalysis?>(null) }

    Box(modifier = modifier) {
        AndroidView(factory = { ctx ->
            PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FIT_CENTER
            }.also {
                onPreviewChanged(it)
                preview.value = Preview.Builder().build().also { preview ->
                    preview.setSurfaceProvider(it.surfaceProvider)
                }
                imageAnalysis.value = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                    .also { imageAnalysis ->
                        imageAnalysis.setAnalyzer(
                            Executors.newSingleThreadExecutor(), imageAnalyser
                        )
                    }

                cameraProviderFuture.addListener({
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector.value, preview.value, imageAnalysis.value
                    )
                }, executor)
            }
        })
        FloatingActionButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(12.dp),
            onClick = {
                cameraSelector.value =
                    if (cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector.value, preview.value, imageAnalysis.value
                )
            }) {
            Text("switch")
        }
    }
}