package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.unit.dp


@Composable
fun PushScreen(){
    Scaffold() {
        Text("Push Screen", modifier = Modifier.padding(24.dp))
    }
}