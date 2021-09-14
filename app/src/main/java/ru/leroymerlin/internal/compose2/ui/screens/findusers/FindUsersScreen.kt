package ru.leroymerlin.internal.compose2.ui.screens.findusers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.leroymerlin.internal.compose2.Navigation
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.ui.screens.DetailsScreen
import ru.leroymerlin.internal.compose2.ui.screens.ListScreen
import ru.leroymerlin.internal.compose2.ui.screens.SearchScreen
import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsScreen
import ru.leroymerlin.internal.compose2.ui.screens.cards.EmptyCard
import ru.leroymerlin.internal.compose2.ui.screens.cards.ExpandableCard
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginScreen
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginViewModel
import ru.leroymerlin.internal.compose2.ui.screens.settings.SettingsScreen
import ru.leroymerlin.internal.compose2.withIO


@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun FindUsersScreen( findUsersViewModel: FindUsersViewModel, navController:NavController){
    val cards:List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
    val error:String by findUsersViewModel.error.observeAsState("")
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold {
     /*   topBar = {
            TopAppBar{
                Text(text = "text")
            }
        }*/
            val textState = remember { mutableStateOf("")}
            val isEnabled = remember { mutableStateOf(false)}
            val isCompleteLogin = remember { mutableStateOf(0)}

        val cards:List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
        val expandedCardIds = findUsersViewModel.expandedCardIdsList.observeAsState()
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "") //достаем данные из shared prefs
        val authHeader = "Bearer " + token

        Column {
          //  Text("Поиск по сотруднику", modifier = Modifier.padding(24.dp))

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
                    //trailingIcon = { if(textState.value.length ==8){ Text("V")} }, //при вводе 8 знаков появится иконка
                    modifier = Modifier
                        .padding(8.dp)
                       // .height(50.dp)
                )



                Button(onClick = {
                    val isDigits = TextUtils.isDigitsOnly(textState.value) //проверка - является ли числом

                    if(textState.value.length == 8 && isDigits){
                        findUsersViewModel.getInfoUser(ldap = textState.value, authHeader = authHeader)
                    }else{
                        findUsersViewModel.getUserByName(userName =textState.value, authHeader = authHeader)
                    }
                    keyboardController?.hide()
             //  Log.e("isEnabled", isEnabled.value.toString())
                   // isEnabled.value = isEnabled.value==false

                },colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(50.dp)) {
                    Text("Поиск", color = Color.White)
                }


        }



            if (error.isNotBlank()) {
                if(error.contains("401", ignoreCase = true)){
                    Log.e("error - ", "401")

                    with (sharedPref.edit()) {
                        //  Log.e("remove sharepref", "- true")
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


                    // Toast.makeText(context, "Нужно перезайти", Toast.LENGTH_SHORT).show()
                   // GlobalScope.async() { exitApp(activity) }
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
               // Log.e(" if cards -", cards.toString())

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
