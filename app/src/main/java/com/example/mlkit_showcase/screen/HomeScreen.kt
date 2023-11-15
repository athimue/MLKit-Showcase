package com.example.mlkit_showcase.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onTextRecognitionClick: () -> Unit,
    onObjectDetectionClick: () -> Unit,
    onImageLabelingClick: () -> Unit,
    onBarcodeScanningClick: () -> Unit,
    onFaceDetectionClick: () -> Unit,
    onDigitalInkRecognitionClick: () -> Unit,
    onPoseDetectionClick: () -> Unit,
    onSelfieSegmentationClick: () -> Unit,
) {
    Column(
    ) {
        Text(
            text = "ML KIT SHOWCASE",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = onTextRecognitionClick
            ) {
                Text("Text recognition")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = onObjectDetectionClick
            ) {
                Text("Objects detection")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = onImageLabelingClick
            ) {
                Text("Image labeling")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = onBarcodeScanningClick
            ) {
                Text("Barcode scanner")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = onFaceDetectionClick
            ) {
                Text("Face detection")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = {}
            ) {
                Text("Face mesh detection")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                shape = RectangleShape,
                onClick = onDigitalInkRecognitionClick
            ) {
                Text("Digital ink recognition")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = onPoseDetectionClick
            ) {
                Text("Pose detection")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                shape = RectangleShape,
                onClick = onSelfieSegmentationClick
            ) {
                Text("Selfie segmentation")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                shape = RectangleShape,
                onClick = { }
            ) {
                Text("Subject segmentation")
            }
        }
    }
}