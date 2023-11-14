package com.example.mlkit_showcase.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mlkit_showcase.analyser.TextRecognitionAnalyser
import com.example.mlkit_showcase.composable.CameraView
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.example.mlkit_showcase.composable.TopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TextRecognitionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Scaffold(topBar = {
        TopBar(onBackClick = onBackClick, text = "Text Recognition")
    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (cameraPermissionState.status.isGranted) {
                TextRecognitionContent()
            } else {
                NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
            }
        }
    })
}

@Composable
fun TextRecognitionContent(
    modifier: Modifier = Modifier
) {
    val textValue = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.scrollable(scrollState, orientation = Orientation.Vertical)
    ) {
        CameraView(
            modifier = modifier.weight(1f),
            imageAnalyser = TextRecognitionAnalyser { textValue.value = it })
        Column(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = "Words detected",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Text(
                text = textValue.value,
            )
        }
    }
}