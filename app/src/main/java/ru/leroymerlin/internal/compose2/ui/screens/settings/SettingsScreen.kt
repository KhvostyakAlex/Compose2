package ru.leroymerlin.internal.compose2.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.leroymerlin.internal.compose2.BuildConfig
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.ui.screens.DetailsScreen
import ru.leroymerlin.internal.compose2.ui.screens.ListScreen
import ru.leroymerlin.internal.compose2.ui.screens.SearchScreen
import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsScreen
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginScreen
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginViewModel
import ru.leroymerlin.internal.compose2.withIO


@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SettingsScreen( navController:NavController) {
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val versionName: String = BuildConfig.VERSION_NAME

    //Log.e("navController.currentDestination.route-", navController.currentDestination?.route.toString())
    Scaffold() {
        Column(//verticalArrangement = Arrangement.Center,
           modifier= Modifier.fillMaxWidth()
        ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            ExitButton(navController = navController, activity=activity)
        }
            getSettings(activity = activity, model = SettingsViewModel())
            Divider()
            SettingsView(settingsViewModel = SettingsViewModel(), activity = activity)
            Row(modifier= Modifier.fillMaxHeight().padding(start = 12.dp, bottom = 60.dp).weight(1f),
                verticalAlignment = Alignment.Bottom) {
                Text("Версия программы $versionName", fontSize = 10.sp )
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
fun ExitButton(navController: NavController, activity: Activity   ){
    val onClick = { }
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val signin = sharedPref.getBoolean("signin?", false) //достаем данные из shared prefs
    val token = sharedPref.getString("token", "") //достаем данные из shared prefs
    val authHeader = sharedPref.getString("authHeader", "") //достаем данные из shared prefs
    Log.e("setting signin - ", signin.toString())
    Log.e("setting - ", "authHeader -"+ authHeader.toString())

//Simple FAB
    FloatingActionButton(onClick = {
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

        //GlobalScope.async() { exitApp(navController) }
                //  navController.navigate("login")

                                   },
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
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun Navigation(navController: NavHostController,
               loginViewModel: LoginViewModel,
               cardsViewModel: CardsViewModel
){
    NavHost(navController = navController, startDestination = "search"){
        composable("login"){ LoginScreen(loginViewModel, navController) }
        composable("list"){ ListScreen(navController) }
        composable("search"){ SearchScreen() }
        composable("cards"){ CardsScreen(cardsViewModel) }
        composable("details"){ DetailsScreen() }
        composable("settings"){ SettingsScreen(navController) }
    }

}


suspend fun exitApp(navController: NavController) {
    Log.e("exitApp-", "true")
   // Log.e("navController.currentDestination.route-", navController.currentDestination?.route.toString())
   // val navController: NavController = NavController(activity)
    // val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    //val activity = LocalContext.current as Activity
    //val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    //val myEditor = sharedPref.edit()

    navController.navigate("login"){
       // launchSingleTop = true //переходим только 1 раз

    //runs on worker thread and returns data
   /* withContext(Dispatchers.Main){
        withIO {
            //выполняется в фоне
            myEditor.remove("signin?").apply()
            myEditor.remove("token?").apply()
            myEditor.remove("authHeader?").commit()
        }
        navController.navigate("login"){
            launchSingleTop = true //переходим только 1 раз
        }*/

    }
}
