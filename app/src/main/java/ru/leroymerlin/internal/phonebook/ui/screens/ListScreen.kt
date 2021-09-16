package ru.leroymerlin.internal.phonebook.ui.screens




import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController


data class Word (val value: String)

@Composable
fun ListScreen(navController: NavController){
    val testArray = listOf("Hello", "World", "Android", "Hello", "World", "Android", "Hello", "World", "Android", "Android", "Hello", "World", "Android")
    Scaffold() {
        //Text("List Screen", modifier = Modifier.padding(24.dp))

       /* LazyColumn(){
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
                }
            }
        }*/

        val arrJobTitleList = listOf(
            "Все",
            "менеджер отдела",
            "продавец-консультант",
            "руководитель торгового сектора",
            "администратор цепи поставок магазина",
            "специалист по администрированию персонала",
            "менеджер сектора по обслуживанию клиентов",
            "специалист цепи поставок магазина",
            "менеджер по охране труда",
            "кассир-консультант",
            "менеджер цепи поставок магазина",
            "специалист технической поддержки",
            "механик",
            "менеджер по административным и бухгалтерским вопросам",
            "специалист по продажам",
            "инженер-энергетик",
            "техник-консультант",
            "Директор магазина",
            "руководитель сектора по обслуживанию клиентов",
            "руководитель цепи поставок магазина",
            "инженер-теплотехник",
            "контролер управления",
            "дизайнер"
        )

        var expanded by remember { mutableStateOf(false) }
        val suggestions = listOf("Item1","Item2","Item3")
        var selectedText by remember { mutableStateOf("") }

        var textfieldSize by remember { mutableStateOf(Size.Zero)}

        val icon = if (expanded)
            Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
        else
            Icons.Filled.ArrowDropDown
//при запуске


        Column() {
            LaunchedEffect(Unit){
                selectedText = "text"

            }

            OutlinedTextField(
                value = selectedText,
                onValueChange = { selectedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                label = {Text("Должность")},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){textfieldSize.width.toDp()})
            ) {
                arrJobTitleList.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedText = label
                        expanded=false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }



    }
}


