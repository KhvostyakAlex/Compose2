package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.unit.dp


@Composable
fun PushScreen(){
    Scaffold() {
        Text("Push Screen", modifier = Modifier.padding(24.dp))
        Card(elevation = 8.dp,
        backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                //CardTitle(title = card.title)
                Text("Hello")
                Text("Hello")

                // CardArrow(degrees = arrowRotationDegree, onClick = onCardArrowClick)

            }
        }

    }
}