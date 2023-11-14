package com.example.mlkit_showcase.analyser

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectRecognitionAnalyser(
    private val onObjectsDetected: (List<DetectedObject>) -> Unit
) : ImageAnalysis.Analyzer {

    private val objectDetector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            objectDetector.process(image).addOnSuccessListener { detectedObjects ->
                onObjectsDetected(detectedObjects)
                imageProxy.close()
            }.addOnFailureListener { e ->
                print(e)
                imageProxy.close()
            }
        }
    }
}