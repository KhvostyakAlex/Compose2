package ru.leroymerlin.internal.compose2.ui.screens.finddepartment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.ui.screens.cards.ExpandableCard


@Composable
fun FindDepartmentScreen( viewModel: CardsViewModel){

    Scaffold {
            val textState = remember { mutableStateOf("")}
            val isEnabled = remember { mutableStateOf(false)}
            val isCompleteLogin = remember { mutableStateOf(0)}

        val bottomItems = listOf("list", "search", "push", "cards")
        val textStateLogin = remember { mutableStateOf("") }


        Column {

            Row (
               // horizontalArrangement = Arrangement.SpaceAround
            verticalAlignment = Alignment.CenterVertically
                    ){
               // Text("Строка поиска", modifier = Modifier.padding(24.dp))
                TextField(value = textState.value,
                    onValueChange = { value -> textState.value = value},
                    placeholder = {Text("Ввведи Фамилию/LDAP")},
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colorResource(id = R.color.colorLightGrey),
                    focusedIndicatorColor =  colorResource(id = R.color.lmNCKD),                   //Color.Transparent - hide the indicator
                 //   unfocusedIndicatorColor = Color.Cyan
                    ),
                   // trailingIcon = { if(textState.value.length ==8){ Text("V")} }, //при вводе 8 знаков появится иконка
                    modifier = Modifier.padding(8.dp)
                )

                Button(onClick = {
                    isEnabled.value = isEnabled.value==false

                },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(50.dp)) {
                    Text("Поиск", color = Color.White)
                }


        }

            //если пришли данные, то показываем список
            if(isEnabled.value){

            }


        }

    }
}