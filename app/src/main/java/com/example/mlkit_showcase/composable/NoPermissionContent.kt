package com.example.mlkit_showcase.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NoPermissionContent(
    onPermissionClick: () -> Unit
) {
    Column {
        val textToShow =
            "Camera permission required for this feature to be available. Please grant the permission"
        Text(textToShow)
        Button(onClick = onPermissionClick) {
            Text("Request permission")
        }
    }
}