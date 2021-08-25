package ru.leroymerlin.internal.compose2.ui.screens




import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController

import androidx.navigation.compose.rememberNavController
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel

data class Word (val value: String)

@Composable
fun ListScreen(navController: NavController){
    val testArray = listOf("Hello", "World", "Android", "Hello", "World", "Android", "Hello", "World", "Android", "Android", "Hello", "World", "Android")
    Scaffold() {
        //Text("List Screen", modifier = Modifier.padding(24.dp))

        LazyColumn(){
            testArray.map {
                item {
                        Card(
                            elevation = 8.dp,
                            backgroundColor = Color.White,
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        ) {
                            Text(it, modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("details")
                                })
                        }


                /*Text(it, modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("details")
                        })*/
                }
            }
        }

    }
}


