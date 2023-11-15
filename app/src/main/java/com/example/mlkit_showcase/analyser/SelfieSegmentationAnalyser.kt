package com.example.mlkit_showcase.analyser

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.mlkit_showcase.navigation.Screen
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class SelfieSegmentationAnalyser(
    private val onDetection: (SegmentationMask) -> Unit
) : ImageAnalysis.Analyzer {

    private val realTimeOptions = SelfieSegmenterOptions.Builder()
        .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
        .enableRawSizeMask()
        .build()

    private val detector = Segmentation.getClient(realTimeOptions)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            detector.process(image).addOnSuccessListener { segmentationMask ->
                onDetection(segmentationMask)
                imageProxy.close()
            }
        }
    }
}