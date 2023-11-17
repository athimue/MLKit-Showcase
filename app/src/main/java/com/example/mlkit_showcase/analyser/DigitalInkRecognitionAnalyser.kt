package com.example.mlkit_showcase.analyser

import android.util.Log
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import com.google.mlkit.vision.digitalink.RecognitionResult

class DigitalInkRecognitionAnalyser(
    private val onDetection: (RecognitionResult) -> Unit,
    private val loadFinished: (Boolean) -> Unit
) {

    private var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
    private var recognizer: DigitalInkRecognizer? = null

    init {
        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        } catch (e: MlKitException) {
            Log.d("COUCOU", e.toString())
        }
        if (modelIdentifier == null) {
            Log.d("COUCOU", "no model was found, handle error")
        }

        modelIdentifier?.let {

            val model: DigitalInkRecognitionModel = DigitalInkRecognitionModel.builder(it).build()
            val remoteModelManager = RemoteModelManager.getInstance()

            remoteModelManager.isModelDownloaded(model).addOnSuccessListener { result ->
                Log.d("coucou", "ici $result")
                if (result) {
                    recognizer = DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build()
                    )
                    loadFinished(true)
                } else {
                    remoteModelManager.download(model, DownloadConditions.Builder().build())
                        .addOnSuccessListener {
                            Log.d("COUCOU", "SUCCESS")
                            recognizer = DigitalInkRecognition.getClient(
                                DigitalInkRecognizerOptions.builder(model).build()
                            )
                            loadFinished(true)
                        }.addOnFailureListener { e: Exception ->
                            Log.d("COUCOU", "Error while downloading a model: $e")
                        }
                }
            }.addOnFailureListener {
                Log.d("COUCOU", "error")
            }
        }
    }

    fun analyze(ink: Ink) {
        Log.d("COUCOU", "la : $ink")
        recognizer?.recognize(ink)?.addOnSuccessListener { recognitionResult ->
            Log.d("COUCOU", "here")
            Log.d("coucou", recognitionResult.toString())
            onDetection(recognitionResult)
        }
    }
}