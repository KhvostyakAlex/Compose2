package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.unit.dp


@Composable
fun SearchScreen(){
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

            Row {
               // Text("Строка поиска", modifier = Modifier.padding(24.dp))
                TextField(value = textState.value,
                    onValueChange = { value -> textState.value = value},
                    placeholder = {Text("Ввведи Фамилию/LDAP")},
                    trailingIcon = { if(textState.value.length ==8){ Text("V") } } //при вводе 8 знаков появится иконка

                )



                Button(onClick = {
                    isEnabled.value = isEnabled.value==false

                },colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
                    Text("Поиск", color = Color.White)
                }


        }
            if(isEnabled.value){
                Text("что-то ищем", modifier = Modifier.padding(24.dp))
            }


        }

    }
}