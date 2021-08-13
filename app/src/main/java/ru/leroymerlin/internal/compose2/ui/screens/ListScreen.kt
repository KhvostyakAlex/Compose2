package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class Word (val value: String)

@Composable
fun ListScreen(navController: NavController){
    val testArray = listOf("Hello", "World", "Android", "Hello", "World", "Android", "Hello", "World", "Android", "Android", "Hello", "World", "Android")
    Scaffold() {
        //Text("List Screen", modifier = Modifier.padding(24.dp))

        LazyColumn(){
            testArray.map {
                item {
                    Text(it, modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .clickable {
                          navController.navigate("details")
                        })
                }
            }
        }

    }
}