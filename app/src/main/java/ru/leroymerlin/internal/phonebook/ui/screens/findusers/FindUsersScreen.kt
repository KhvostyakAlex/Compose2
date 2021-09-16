package ru.leroymerlin.internal.phonebook.ui.screens.findusers

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.ui.screens.cards.EmptyCard
import ru.leroymerlin.internal.phonebook.ui.screens.cards.ExpandableCard
import ru.leroymerlin.internal.phonebook.withIO
import java.sql.Timestamp
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList


@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun FindUsersScreen( findUsersViewModel: FindUsersViewModel, navController:NavController){
    //val cards:List<IntraruUserDataList> by findUsersViewModel.cards.observeAsState(emptyList())
    val tokenData:List<IntraruAuthUserData> by findUsersViewModel.tokenData.observeAsState(emptyList())
    val depData:List<String> by findUsersViewModel.depData.observeAsState(emptyList())
    val error:String by findUsersViewModel.error.observeAsState("")
    val activity = LocalContext.current as Activity
    //val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val keyboardController = LocalSoftwareKeyboardController.current


    // Write a message to the database
    // Write a message to the database
    //val database = FirebaseDatabase.getInstance()
    //val myRef = database.getReference("FindUser").child("account")
    //myRef.setValue("Hello, signin!")
    var dataForFirebase = mutableMapOf<String, String>()
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("signin").child("account")
    dataForFirebase.put("account", "60032246")
    dataForFirebase.put("date", "2021-09-16")
    myRef.updateChildren(dataForFirebase as Map<String, Any>)





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
        val token = sharedPref.getString("token", "").toString() //достаем данные из shared prefs
        val refreshToken = sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs
        val expiresIn = sharedPref.getInt("expiresIn", 0) //достаем данные из shared prefs
        val expiresOn = sharedPref.getInt("expiresOn", 0) //достаем данные из shared prefs
        val authHeader = "Bearer " + token



        //val currenTime = Timestamp(System.currentTimeMillis())//1631763871069
        val currenTime = System.currentTimeMillis()/1000
        Log.e("currentTime", currenTime.toString())
        Log.e("expiresIn ", expiresIn.toString())
        Log.e("expiresOn", expiresOn.toString())
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
                },colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(54.dp)) {
                    Text("Поиск", color = Color.White)
                }
        }

            if (error.isNotBlank()) {
                //Ошибка 401 когда заканчивается сессия и нужно перезайти
                if(error.contains("401", ignoreCase = true)){
                    Log.e("!!!!!!!!!!error", "true 401")
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
                    }
                /*вернуть   with (sharedPref.edit()) {
                        remove("signin?")
                        remove("token")
                        remove("authHeader")
                        commit()
                    }
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }*/

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
