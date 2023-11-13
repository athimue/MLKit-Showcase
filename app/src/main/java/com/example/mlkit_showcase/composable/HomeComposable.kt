package com.example.mlkit_showcase.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeComposable(
    onTextRecognitionClick: () -> Unit
) {

    Column {
        Text("HOME")
        Row {
            Button(onClick = onTextRecognitionClick) {
                Text("Text Recognition")
            }
        }
    }
}