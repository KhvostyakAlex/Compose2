package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
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
           // Text("Поиск по подразделению", modifier = Modifier.padding(24.dp))

        /*    Card(elevation = 8.dp,
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
            }*/


            Row (
               // horizontalArrangement = Arrangement.SpaceAround
            verticalAlignment = Alignment.CenterVertically
                    ){
               // Text("Строка поиска", modifier = Modifier.padding(24.dp))
                TextField(value = textState.value,
                    onValueChange = { value -> textState.value = value},
                    placeholder = {Text("Ввведи Фамилию/LDAP")},
                    trailingIcon = { if(textState.value.length ==8){ Text("V")} }, //при вводе 8 знаков появится иконка
                    modifier = Modifier.padding(8.dp).height(50.dp)
                )

                Button(onClick = {
                    isEnabled.value = isEnabled.value==false

                },colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text("Поиск", color = Color.White)
                }


        }

            //если пришли данные, то показываем список
            if(isEnabled.value){
             /*   val cards = viewModel.cards.collectAsState()
                val expandedCardIds = viewModel.expandedCardIdsList.collectAsState()
                LazyColumn {
                    itemsIndexed(cards.value) { _, card ->
                        ExpandableCard(
                            card = card,
                            onCardArrowClick = { viewModel.onCardArrowClicked(card.account.toInt()) },
                            expanded = expandedCardIds.value.contains(card.account.toInt()),
                        )
                    }
                }

              */
            }


        }

    }
}