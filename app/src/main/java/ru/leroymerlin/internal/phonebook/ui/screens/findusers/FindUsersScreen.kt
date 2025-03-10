package ru.leroymerlin.internal.phonebook.ui.screens.findusers

import android.app.Activity
import android.content.Context
import android.text.TextUtils
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.ui.screens.cards.EmptyCard
import ru.leroymerlin.internal.phonebook.ui.screens.cards.ExpandableCard
import ru.leroymerlin.internal.phonebook.addToFB
import ru.leroymerlin.internal.phonebook.ui.themes.JetPhonebookTheme

@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun FindUsersScreen( findUsersViewModel: FindUsersViewModel,
                     navController:NavController,
modifier:Modifier) {
    val error: String by findUsersViewModel.error.observeAsState("")
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val keyboardController = LocalSoftwareKeyboardController.current
    val textState = remember { mutableStateOf("") }
    val cards: List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
    val expandedCardIds = findUsersViewModel.expandedCardIdsList.observeAsState()
    val token = sharedPref.getString("token", "").toString() //достаем данные из shared prefs
    val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs
    val authHeader = "Bearer " + token
    Surface(
        modifier = modifier,
        color = JetPhonebookTheme.colors.primaryBackground,
    ) {

    Scaffold (
        backgroundColor = JetPhonebookTheme.colors.secondaryBackground
            ){
        Column {

            Row(
                // horizontalArrangement = Arrangement.SpaceAround
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textState.value,
                    onValueChange = { value -> textState.value = value },
                    placeholder = { Text(
                        "Ввведи Фамилию/LDAP",
                        color = JetPhonebookTheme.colors.primaryText,
                        style =  JetPhonebookTheme.typography.caption
                    ) },

                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                       // backgroundColor = colorResource(id = R.color.colorLightGrey),
                        backgroundColor = JetPhonebookTheme.colors.primaryBackground,
                        focusedIndicatorColor = Color.Transparent,                   //Color.Transparent - hide the indicator
                        unfocusedIndicatorColor = Color.Transparent,
                        textColor = JetPhonebookTheme.colors.primaryText,
                        cursorColor= JetPhonebookTheme.colors.thirdText,
                        focusedLabelColor = JetPhonebookTheme.colors.thirdText
                    ),
                    modifier = Modifier.padding(8.dp),

                )

                Button(
                    onClick = {
                        val isDigits =
                            TextUtils.isDigitsOnly(textState.value) //проверка - является ли числом

                        if (textState.value.length == 8 && isDigits) {
                            findUsersViewModel.getInfoUser(
                                ldap = textState.value,
                                authHeader = authHeader
                            )
                        } else {
                            findUsersViewModel.getUserByName(
                                userName = textState.value,
                                authHeader = authHeader
                            )
                        }
                        keyboardController?.hide()
                        //добавляем в аналитику
                        addToFB("FindUser", account, textState.value)

                    },
                   // colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    colors = ButtonDefaults.buttonColors(backgroundColor = JetPhonebookTheme.colors.primaryBackground),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(end = 8.dp).height(56.dp)
                ) {
                    Text("Поиск",
                        color = JetPhonebookTheme.colors.primaryText,
                        style = JetPhonebookTheme.typography.toolbar)
                }
            }

            //если пришли данные, то показываем список
            if (cards.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(cards) { _, card ->
                        ExpandableCard(
                            card = card,
                            onCardArrowClick = { findUsersViewModel.onCardArrowClicked(card.account.toInt()) },
                            expanded = expandedCardIds.value!!.contains(card.account.toInt()),
                        )
                    }
                }
            } else {
                EmptyCard("Ничего не найдено")
            }

            //обработка ошибок
            if (error.isNotBlank()) {
                //Ошибка 401 когда заканчивается сессия и нужно перезайти
                if (error.contains("401", ignoreCase = true)) {
                    //  refreshToken(activity, findUsersViewModel, refreshToken,navController)
                }
            }
        }
    }
}
}

          /*  @Composable
            fun refreshToken(activity: Activity,
                             findUsersViewModel: FindUsersViewModel,
                             refreshToken:String,
                             navController: NavController) {
                val tokenData:List<IntraruAuthUserData> by findUsersViewModel.tokenData.observeAsState(emptyList())
                val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
                findUsersViewModel.refreshToken(refreshToken)
                if(tokenData.isNotEmpty()){
                    Log.e("tokenData - ", tokenData.toString())
                    val t = tokenData[0]

                    with (sharedPref.edit()) {
                        putString("token", t.token)
                        putString("refreshToken", t.refreshToken)
                        putInt("expiresIn", t.expiresIn)
                        putInt("expiresOn", t.expiresOn)
                        putString("authHeader", "Bearer " + t.token)
                        apply()
                    }
                    Toast.makeText(activity, "Перезаходим в приложение, попробуй еще раз.", Toast.LENGTH_SHORT).show()
                    navController.navigate("search") {
                        popUpTo("search") {
                            inclusive = true
                        }
                    }
                }
            }*/

