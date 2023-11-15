package com.example.mlkit_showcase.screen

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.mlkit_showcase.analyser.BarcodeScanningAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.example.mlkit_showcase.composable.TableCell
import com.example.mlkit_showcase.composable.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.common.Barcode

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScanningScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Scaffold(topBar = {
        TopBar(onBackClick = onBackClick, text = "Barcode scanner")
    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (cameraPermissionState.status.isGranted) {
                BarcodeScanningContent()
            } else {
                NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
            }
        }
    })
}

@Composable
fun BarcodeScanningContent(
    modifier: Modifier = Modifier
) {
    var barcodes by remember { mutableStateOf(listOf<Barcode>()) }
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.scrollable(scrollState, orientation = Orientation.Vertical)
    ) {
        CameraView(modifier = modifier.weight(1f),
            imageAnalyser = BarcodeScanningAnalyser {
                barcodes = it
            }
        )
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
                text = "Barcode detected",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Divider()
            if (barcodes.isNotEmpty()) {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(text = "Format", weight = 0.22f)
                            TableCell(text = "Value", weight = 0.78f)
                        }
                    }
                    items(items = barcodes) { barcode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            TableCell(text = barcode.format.toString(), weight = 0.22f)
                            TableCell(
                                text = barcode.displayValue.toString(), weight = 0.78f
                            )
                        }
                    }
                }
            } else {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    text = "No barcode",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}
