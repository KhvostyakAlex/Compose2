package ru.leroymerlin.internal.compose2.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.leroymerlin.internal.compose2.R



@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SettingsScreen() {
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

    Scaffold() {
        Column(//verticalArrangement = Arrangement.Center,
           modifier= Modifier.fillMaxWidth()
        ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            ExitButton()
        }
            getSettings(activity = activity, model = SettingsViewModel())
            Divider()
            SettingsView(settingsViewModel = SettingsViewModel(), activity = activity)
            Row(modifier= Modifier.fillMaxHeight().padding(start = 12.dp, bottom = 60.dp).weight(1f),
                verticalAlignment = Alignment.Bottom) {
                Text("Версия программы 1.7.5", fontSize = 10.sp )
            }
        }
    }
}

fun getSettings(activity:Activity, model: SettingsViewModel) {
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val token = sharedPref.getString("token", "") //достаем данные из shared prefs
    // Log.e("sharePrefs", token.toString())

}

@Composable
fun ExitBtn() {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.ExitToApp,"") },
        text = { Text("Выход") },
        onClick = { /*do something*/ },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}


@Composable
fun ExitButton(){
    val onClick = { /* Do something */ }

//Simple FAB
    FloatingActionButton(onClick = onClick,
        modifier= Modifier.padding(8.dp),
    backgroundColor = colorResource(id = R.color.lmNCKD)) {
        Icon(Icons.Filled.ExitToApp,"")
    }
}

@Composable
fun SettingsView(settingsViewModel: SettingsViewModel, activity: Activity){
   // val settings by settingsViewModel.settings.observeAsState(emptyList())

    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val firstName = sharedPref.getString("firstName", "") //достаем данные из shared prefs
    val lastName = sharedPref.getString("lastName", "") //достаем данные из shared prefs
    val shopNumber = sharedPref.getString("shopNumber", "") //достаем данные из shared prefs
    val jobTitle = sharedPref.getString("jobTitle", "") //достаем данные из shared prefs
    val workPhone = sharedPref.getString("workPhone", "") //достаем данные из shared prefs


    val settings =   listOf(
        SettingsModel("Имя", "${firstName.toString()} ${lastName.toString()}"),
        SettingsModel("№ магазина", shopNumber.toString()),
        SettingsModel("Должность", jobTitle.toString()),
        SettingsModel("Телефон", workPhone.toString()))


    Column(modifier = Modifier.fillMaxWidth()) {
        settings.map{ SettingsCell(model = it)}
    }
}

@Composable
fun SettingsCell(model: SettingsModel){
Column() {
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
