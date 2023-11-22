package com.example.mlkit_showcase.analyser

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions

class LanguageIdentificationAnalyser(
    private val onDetection: (List<Pair<String, Float>>) -> Unit
) {

    private val languageIdentifier = LanguageIdentification
        .getClient(LanguageIdentificationOptions.Builder().build())

    fun analyze(text: String) {
        languageIdentifier.identifyPossibleLanguages(text)
            .addOnSuccessListener { identifiedLanguages ->
                onDetection(
                    identifiedLanguages.map { Pair(it.languageTag, it.confidence) })
            }
    }
}