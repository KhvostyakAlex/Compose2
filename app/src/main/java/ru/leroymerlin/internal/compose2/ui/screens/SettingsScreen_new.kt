package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi




@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SettingsScreen_new() {
    Scaffold() {

        val bottomItems = listOf("list", "search", "push", "cards")
        val textStateLogin = remember { mutableStateOf("") }
        Text("Push Screen", modifier = Modifier.padding(24.dp))
        Card(elevation = 8.dp,
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                //CardTitle(title = card.title)
                AutoCompleteText("value: String",
                    { textStateLogin.value = it },
                    { textStateLogin.value = it },
                    Modifier,
                    null,
                    bottomItems)

                // CardArrow(degrees = arrowRotationDegree, onClick = onCardArrowClick)

            }
        }

    }
}