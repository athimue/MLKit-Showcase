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
    image: ImageProxy? = null,
    rect: Rect? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val cameraProvider = cameraProviderFuture.get()
    val executor = ContextCompat.getMainExecutor(context)
    val cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }
    val preview = remember { mutableStateOf<Preview?>(null) }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val imageAnalysis = remember { mutableStateOf<ImageAnalysis?>(null) }
    var composeRect: androidx.compose.ui.geometry.Rect

    Box(modifier = modifier) {
        AndroidView(factory = { ctx ->
            PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FIT_CENTER
            }.also {
                previewView.value = it
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
        image?.let { img ->
            rect?.let { points ->

                val imageAspectRatio = img.width.toFloat() / img.height
                val previewAspectRatio =
                    previewView.value?.width?.toFloat()!! / previewView.value?.height?.toFloat()!!

                val scale = if (imageAspectRatio < previewAspectRatio) {
                    previewView.value?.height?.toFloat()!! / img.height
                } else {
                    previewView.value?.width?.toFloat()!! / img.width
                }
                val offsetX = 150

                val transformedRect = Rect(
                    (points.left * scale + offsetX).toInt(),
                    (points.top * scale).toInt(),
                    (points.right * scale + offsetX).toInt(),
                    (points.bottom * scale).toInt()
                )

                composeRect = androidx.compose.ui.geometry.Rect(
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
        FloatingActionButton(
            modifier = Modifier
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
            Text("+")
        }
    }
}