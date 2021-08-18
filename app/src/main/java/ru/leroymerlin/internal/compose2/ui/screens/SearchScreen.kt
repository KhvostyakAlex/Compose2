package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun SearchScreen(navController: NavController){

    val testArray = listOf("Иванов", "Петров", "Сидоров", "Васечкин", "World", "Android", "Hello", "World", "Android", "Android", "Hello", "World", "Android")
    Scaffold {
     /*   topBar = {
            TopAppBar{
                Text(text = "text")
            }
        }*/
            val textState = remember { mutableStateOf("")}
            val isEnabled = remember { mutableStateOf(false)}
            val isCompleteLogin = remember { mutableStateOf(0)}



        Column {
            Text("Search Screen", modifier = Modifier.padding(24.dp))

            Row (
               // horizontalArrangement = Arrangement.SpaceAround
            verticalAlignment = Alignment.CenterVertically
                    ){
               // Text("Строка поиска", modifier = Modifier.padding(24.dp))
                TextField(value = textState.value,
                    onValueChange = { value -> textState.value = value},
                    placeholder = {Text("Ввведи Фамилию/LDAP")},
                    trailingIcon = { if(textState.value.length ==8){ Text("V")} }, //при вводе 8 знаков появится иконка
                    modifier = Modifier.padding(8.dp)
                )



                Button(onClick = {
                    isEnabled.value = isEnabled.value==false

                },colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text("Поиск", color = Color.White)
                }


        }

            //если пришли данные, то показываем список
            if(isEnabled.value){
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

    }
}