package com.example.mlkit_showcase.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mlkit_showcase.analyser.DigitalInkRecognitionAnalyser
import com.example.mlkit_showcase.composable.NoPermissionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.digitalink.Ink
import com.google.mlkit.vision.digitalink.RecognitionResult
import com.google.mlkit.vision.text.Text

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DigitalInkRecognitionScreen(
    onBackClick: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (cameraPermissionState.status.isGranted) {
            DigitalInkRecognitionContent(onBackClick = onBackClick)
        } else {
            NoPermissionContent(onPermissionClick = { cameraPermissionState.launchPermissionRequest() })
        }
    }
}

@Composable
fun DigitalInkRecognitionContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val lines = remember {
        mutableStateListOf<Line>()
    }
    var isLoaded by remember { mutableStateOf(false) }
    var recognitionResult by remember { mutableStateOf<RecognitionResult?>(null) }
    val digitalInkRecognitionAnalyser = DigitalInkRecognitionAnalyser(onDetection = { reco ->
        run {
            Log.d("COUCOU", "xx")
            recognitionResult = reco
        }
    }, loadFinished = { isLoaded = true })
    val inkBuilder = remember { Ink.Builder() }
    var inkStrokeBuilder = remember { Ink.Stroke.builder() }

    Column {
        if (!isLoaded) {
            CircularProgressIndicator()
        } else {
            Box(modifier = modifier.weight(0.5f)) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    onClick = onBackClick
                ) {
                    Text("Back")
                }
                DrawingScreen(modifier,
                    digitalInkRecognitionAnalyser,
                    lines,
                    inkBuilder,
                    inkStrokeBuilder,
                    addLine = { lines.add(it) },
                    clearInkStrokeBuilder = { inkStrokeBuilder = it })
            }
            Divider()
            Column(modifier = modifier.weight(0.5f)) {
                recognitionResult?.candidates.let { reco ->
                    reco?.let {
                        for (candidate in it) {
                            Text(candidate.text)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawingScreen(
    modifier: Modifier,
    digitalInkRecognitionAnalyser: DigitalInkRecognitionAnalyser,
    lines: List<Line>,
    inkBuilder: Ink.Builder,
    inkStrokeBuilder: Ink.Stroke.Builder,
    addLine: (Line) -> Unit,
    clearInkStrokeBuilder: (Ink.Stroke.Builder) -> Unit,
) {
    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(true) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                val line = Line(
                    start = change.position - dragAmount, end = change.position
                )
                addLine(line)

                inkStrokeBuilder.addPoint(
                    Ink.Point.create(
                        line.start.x, line.start.y, System.currentTimeMillis()
                    )
                )
                inkStrokeBuilder.addPoint(
                    Ink.Point.create(
                        line.end.x, line.end.y, System.currentTimeMillis()
                    )
                )

                if (change.pressed) {
                    inkBuilder.addStroke(inkStrokeBuilder.build())
                    clearInkStrokeBuilder(Ink.Stroke.builder())
                    digitalInkRecognitionAnalyser.analyze(inkBuilder.build())
                }
            }
        }) {
        lines.forEach { line ->
            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

data class Line(
    val start: Offset, val end: Offset, val color: Color = Color.Black, val strokeWidth: Dp = 2.dp
)