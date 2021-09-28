package ru.leroymerlin.internal.phonebook.ui.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.leroymerlin.internal.phonebook.BuildConfig
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.addToFB


@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    navController:NavController,
    isDarkMode:Boolean
) {
    val activity = LocalContext.current as Activity
    val versionName: String = BuildConfig.VERSION_NAME

    Scaffold{
        Column(//verticalArrangement = Arrangement.Center,
           modifier= Modifier.fillMaxWidth()
        ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            ExitButton2(navController = navController, activity=activity)
            ExitButton(navController = navController, activity=activity)
        }
            Divider()
            SettingsView(settingsViewModel = SettingsViewModel(), activity = activity)
            Row(modifier= Modifier.fillMaxHeight().padding(start = 12.dp, bottom = 60.dp).weight(1f),
                verticalAlignment = Alignment.Bottom) {
                Text("Версия программы $versionName", fontSize = 10.sp )
            }
        }
    }
}

@Composable
fun ExitButton(navController: NavController, activity: Activity){
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs

    FloatingActionButton(onClick = {
        with (sharedPref.edit()) {
          //  Log.e("remove sharepref", "- true")
            remove("signin?")
            remove("token")
            remove("refreshToken")
            remove("authHeader")
            commit()
        }
        //добавляем в аналитику
        addToFB("ExitButton", account)
        navController.popBackStack()
        navController.navigate("login") {
            popUpTo("login") {
                inclusive = true
            }
        }
                                   },
        modifier= Modifier.padding(8.dp),
        backgroundColor = colorResource(id = R.color.lmNCKD)) {
        Icon(Icons.Filled.ExitToApp,"")
    }
}

@Composable
fun SettingsView(settingsViewModel: SettingsViewModel, activity: Activity){

    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val firstName = sharedPref.getString("firstName", "").toString() //достаем данные из shared prefs
    val lastName = sharedPref.getString("lastName", "").toString() //достаем данные из shared prefs
    val orgUnitName = sharedPref.getString("orgUnitName", "").toString() //достаем данные из shared prefs
    val shopNumber = sharedPref.getString("shopNumber", "").toString() //достаем данные из shared prefs
    val jobTitle = sharedPref.getString("jobTitle", "").toString() //достаем данные из shared prefs
    val workPhone = sharedPref.getString("workPhone", "").toString() //достаем данные из shared prefs
    val expiresIn = sharedPref.getInt("expiresIn", 0) //достаем данные из shared prefs
    val expiresOn = sharedPref.getInt("expiresOn", 0) //достаем данные из shared prefs
    val refreshToken = sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs



    val settings =   listOf(
        SettingsModel("Имя", "$firstName $lastName"),
        SettingsModel("Магазин", "$orgUnitName (${shopNumber})"),
        SettingsModel("Должность", jobTitle),
        SettingsModel("Телефон", workPhone),
        SettingsModel("expiresIn", expiresIn.toString()),
        SettingsModel("expiresOn", expiresOn.toString()),
        SettingsModel("refreshToken", refreshToken.toString()))

    Column(modifier = Modifier.fillMaxWidth()) {
        settings.map{ SettingsCell(model = it)}
    }
}

@Composable
fun SettingsCell(model: SettingsModel){
Column{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 24.dp)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically

    ){
        Text(model.title, modifier = Modifier.weight(0.3f), style = TextStyle(color= Color.Black))
        Text(model.value, modifier = Modifier
            .weight(0.7f)
            .padding(8.dp), style = TextStyle(color= Color.Black))
    }
}
    Divider()
}
@Composable
fun ExitButton2(navController: NavController, activity: Activity){
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs

    FloatingActionButton(onClick = {
        with (sharedPref.edit()) {
            //  Log.e("remove sharepref", "- true")
           // remove("signin?")
            remove("token")
          //  remove("refreshToken")
            remove("authHeader")
            commit()
        }
    },
        modifier= Modifier.padding(8.dp),
        contentColor=Color.White,
        backgroundColor = colorResource(id = R.color.colorCopy)) {
        Icon(Icons.Filled.Sync,"")
    }
}
