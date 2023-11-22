package com.example.mlkit_showcase.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mlkit_showcase.analyser.LanguageIdentificationAnalyser
import com.example.mlkit_showcase.composable.TableCell
import com.example.mlkit_showcase.composable.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageIdentificationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var text by remember {
        mutableStateOf("")
    }
    var languagesIdentified by remember {
        mutableStateOf(listOf<Pair<String, Float>>())
    }
    val analyser = LanguageIdentificationAnalyser(onDetection = { languagesIdentified = it })
    Scaffold(topBar = {
        TopBar(onBackClick = onBackClick, text = "Language Identification")
    }, content = {
        Column(modifier = Modifier.padding(it)) {
            Row(modifier = modifier.weight(0.3f)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxSize(),
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        analyser.analyze(newText)
                    },
                    label = { Text("Text") })
            }
            Divider()
            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "LANGUAGES IDENTIFIED",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                if (languagesIdentified.isNotEmpty()) {
                    LazyColumn {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                            ) {
                                TableCell(text = "Language", weight = 0.5f)
                                TableCell(text = "Confidence", weight = 0.5f)
                            }
                        }
                        items(items = languagesIdentified) { languageIdentified ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 8.dp)
                            ) {
                                TableCell(
                                    text = languageIdentified.first,
                                    weight = 0.5f
                                )
                                TableCell(
                                    text = String.format("%.2f", languageIdentified.second),
                                    weight = 0.5f
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        text = "No language identified",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )
                }
            }
        }
    })
}