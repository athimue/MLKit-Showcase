package com.example.mlkit_showcase.analyser

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentationResult
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions

class SubjectSegmentationAnalyser(
    private val onDetection: (SubjectSegmentationResult) -> Unit
) : ImageAnalysis.Analyzer {

    private val subjectResultOptions = SubjectSegmenterOptions.SubjectResultOptions.Builder()
        .enableConfidenceMask()
        .build()

    private val options = SubjectSegmenterOptions.Builder()
        .enableMultipleSubjects(subjectResultOptions)
        .build()

    private val detector = SubjectSegmentation.getClient(options)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            detector.process(image).addOnSuccessListener { subjectSegmentationResult ->
                onDetection(subjectSegmentationResult)
                imageProxy.close()
            }
        }
    }
}