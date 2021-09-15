package ru.leroymerlin.internal.compose2.ui.screens.findusers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.ui.screens.cards.EmptyCard
import ru.leroymerlin.internal.compose2.ui.screens.cards.ExpandableCard
import ru.leroymerlin.internal.compose2.withIO


@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun FindUsersScreen( findUsersViewModel: FindUsersViewModel, navController:NavController){
    //val cards:List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
    val error:String by findUsersViewModel.error.observeAsState("")
    val activity = LocalContext.current as Activity
    //val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold {
     /*   topBar = {
            TopAppBar{
                Text(text = "text")
            }
        }*/
            val textState = remember { mutableStateOf("")}
            //val isEnabled = remember { mutableStateOf(false)}
            //val isCompleteLogin = remember { mutableStateOf(0)}

        val cards:List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
        val expandedCardIds = findUsersViewModel.expandedCardIdsList.observeAsState()
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") //достаем данные из shared prefs
        val authHeader = "Bearer " + token

        Column {
            Row (
               // horizontalArrangement = Arrangement.SpaceAround
                verticalAlignment = Alignment.CenterVertically
                    ){
                TextField(value = textState.value,
                    onValueChange = { value -> textState.value = value},
                    placeholder = {Text("Ввведи Фамилию/LDAP")},
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colorResource(id = R.color.colorLightGrey),
                        focusedIndicatorColor =  Color.Transparent,                   //Color.Transparent - hide the indicator
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                Button(onClick = {
                    val isDigits = TextUtils.isDigitsOnly(textState.value) //проверка - является ли числом

                    if(textState.value.length == 8 && isDigits){
                        findUsersViewModel.getInfoUser(ldap = textState.value, authHeader = authHeader)
                    }else{
                        findUsersViewModel.getUserByName(userName =textState.value, authHeader = authHeader)
                    }
                    keyboardController?.hide()
                },colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(50.dp)) {
                    Text("Поиск", color = Color.White)
                }
        }

            if (error.isNotBlank()) {
                //Ошибка 401 когда заканчивается сессия и нужно перезайти
                if(error.contains("401", ignoreCase = true)){
                    with (sharedPref.edit()) {
                        remove("signin?")
                        remove("token")
                        remove("authHeader")
                        commit()
                    }
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }else if(error.contains("404", ignoreCase = true)){
                    Log.e("error", "true 404")

                    //  Toast.makeText(context, "Такой Ldap не найден", Toast.LENGTH_SHORT).show()
                    val data = ArrayList<IntraruUserDataList>()
                    val cards =  data.add(IntraruUserDataList(
                        "0", "Ничего не найдено", "",
                        "","", "", "",
                        "", "", "", "null",
                        "", "", ""))

                 /*   LazyColumn {
                        itemsIndexed(data) { _, card ->
                            ExpandableCard(
                                card = card,
                                onCardArrowClick = { findUsersViewModel.onCardArrowClicked(card.account.toInt()) },
                                expanded = expandedCardIds.value!!.contains(card.account.toInt()),
                            )
                        }
                    }*/

                }else {
                    //Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

            //если пришли данные, то показываем список
            if(cards.isNotEmpty()){
                LazyColumn {
                    itemsIndexed(cards) { _, card ->
                        ExpandableCard(
                            card = card,
                            onCardArrowClick = { findUsersViewModel.onCardArrowClicked(card.account.toInt()) },
                            expanded = expandedCardIds.value!!.contains(card.account.toInt()),
                        )
                    }
                }
            }else{
                EmptyCard("Ничего не найдено")
            }
        }
    }
}


suspend fun exitApp(activity: Activity) {
    val navController:NavController = NavController(activity)
   // val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    //val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val myEditor: SharedPreferences.Editor = sharedPref.edit()
    //runs on worker thread and returns data
    withContext(Dispatchers.Main){
        withIO {
            //выполняется в фоне
            myEditor.remove("signin?").apply()
            myEditor.remove("token?").apply()
            myEditor.remove("authHeader?").commit()
        }
        navController.navigate("login"){
            launchSingleTop = true //переходим только 1 раз
        }

    }
}
