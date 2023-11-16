package com.example.mlkit_showcase.analyser

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.facemesh.FaceMesh
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions

class FaceMeshDetectionAnalyser(
    private val onDetection: (List<FaceMesh>) -> Unit
) : ImageAnalysis.Analyzer {

    private val realTimeOptions =
        FaceMeshDetectorOptions
            .Builder()
            .setUseCase(FaceMeshDetectorOptions.FACE_MESH)
            .build()

    private val detector = FaceMeshDetection.getClient(realTimeOptions)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            detector.process(inputImage).addOnSuccessListener { result ->
                onDetection(result)
                imageProxy.close()
            }
        }
    }
}