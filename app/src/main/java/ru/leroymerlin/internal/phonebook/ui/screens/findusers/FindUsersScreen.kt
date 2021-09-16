package ru.leroymerlin.internal.phonebook.ui.screens.findusers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.ui.screens.cards.EmptyCard
import ru.leroymerlin.internal.phonebook.ui.screens.cards.ExpandableCard
import ru.leroymerlin.internal.phonebook.withIO
import java.sql.Timestamp

import kotlinx.coroutines.flow.merge
import ru.leroymerlin.internal.phonebook.addToFB
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.phonebook.getCalculatedDate


@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun FindUsersScreen( findUsersViewModel: FindUsersViewModel, navController:NavController){
    val error:String by findUsersViewModel.error.observeAsState("")
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val keyboardController = LocalSoftwareKeyboardController.current
    val textState = remember { mutableStateOf("")}
    val cards:List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
    val expandedCardIds = findUsersViewModel.expandedCardIdsList.observeAsState()
    val token = sharedPref.getString("token", "").toString() //достаем данные из shared prefs
    val refreshToken = sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs
    val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs
    val expiresIn = sharedPref.getInt("expiresIn", 0) //достаем данные из shared prefs
    val expiresOn = sharedPref.getInt("expiresOn", 0) //достаем данные из shared prefs
    val authHeader = "Bearer " + token

    //Firebase
    val today = getCalculatedDate("yyyy-M-dd hh:mm", 0).toString()
    //val myRef = database.getReference("FindUser").child("account")
    //myRef.setValue("Hello, signin!")










            Scaffold {
                /*   topBar = {
                       TopAppBar{
                           Text(text = "text")
                       }
                   }*/

                // Log.e("refr - ", refreshToken.toString())

                /* findUsersViewModel.refreshToken(refreshToken)
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
                 }*/


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
                            //добавляем в аналитику
                            addToFB("FindUser", account, textState.value)

                        },colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(54.dp)) {
                            Text("Поиск", color = Color.White)
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

                    //обработка ошибок
                    if (error.isNotBlank()) {
                        //Ошибка 401 когда заканчивается сессия и нужно перезайти
                        if(error.contains("401", ignoreCase = true)){
                            refreshToken(activity, findUsersViewModel, refreshToken,navController)
                        }else if(error.contains("404", ignoreCase = true)){
                            //Log.e("error", "true 404")
                        }
                    }
                }
            }
        }

            @Composable
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
            }

