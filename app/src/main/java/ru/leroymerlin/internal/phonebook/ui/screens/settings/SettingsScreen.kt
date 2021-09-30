package ru.leroymerlin.internal.phonebook.ui.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.leroymerlin.internal.phonebook.BuildConfig
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.addToFB
import ru.leroymerlin.internal.phonebook.ui.themes.*


@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    navController:NavController,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    currentTextSize: JetPhonebookSize,
    currentPaddingSize: JetPhonebookSize,
    currentCornersStyle: JetPhonebookCorners,
    onDarkModeChanged: (Boolean) -> Unit,
    onNewStyle: (JetPhonebookStyle) -> Unit,
    onTextSizeChanged: (JetPhonebookSize) -> Unit,
    onPaddingSizeChanged: (JetPhonebookSize) -> Unit,
    onCornersStyleChanged: (JetPhonebookCorners) -> Unit,

    ) {
    val activity = LocalContext.current as Activity
    val versionName: String = BuildConfig.VERSION_NAME
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    //Log.e("SettingScreen", isDarkMode.toString())
/*
    Scaffold{
        Column(//verticalArrangement = Arrangement.Center,
           modifier= Modifier.fillMaxWidth()
        ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            ExitButton2(navController = navController, activity=activity)
            ExitButton(navController = navController, activity=activity)
        }

            SettingsView(settingsViewModel = SettingsViewModel(), activity = activity)
            Row(modifier= Modifier.fillMaxHeight().padding(start = 12.dp, bottom = 60.dp).weight(1f),
                verticalAlignment = Alignment.Bottom) {
                Text("Версия программы $versionName", fontSize = 10.sp )
            }
        }
    }
    */


    Surface(
        modifier = modifier,
        color = JetPhonebookTheme.colors.secondaryBackground,
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(
                backgroundColor = JetPhonebookTheme.colors.primaryBackground,
                elevation = 8.dp
            ) {

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = JetPhonebookTheme.shapes.padding),
                    text = stringResource(id = R.string.title_settings),
                    color = JetPhonebookTheme.colors.primaryText,
                    style = JetPhonebookTheme.typography.toolbar
                )
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){
                ExitButton2(navController = navController, activity=activity)
                ExitButton(navController = navController, activity=activity)
            }
            CustomDivider()

            Row(
                modifier = Modifier.padding(JetPhonebookTheme.shapes.padding)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.action_dark_theme_enable),
                    color = JetPhonebookTheme.colors.primaryText,
                    style = JetPhonebookTheme.typography.body
                )
                Checkbox(
                    checked = isDarkMode, onCheckedChange = {
                        onDarkModeChanged.invoke(it)
                        //записываем в pref, чтобы данные сохранмлись при перезагрузке апп
                        with (sharedPref.edit()) {
                            putBoolean("isDarkModeValue", it)
                            apply()
                        }

                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = JetPhonebookTheme.colors.thirdText,
                        uncheckedColor = JetPhonebookTheme.colors.secondaryText
                    )
                )
            }
            CustomDivider()

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_font_size),
                    currentIndex = when (currentTextSize) {
                        JetPhonebookSize.Small -> 0
                        JetPhonebookSize.Medium -> 1
                        JetPhonebookSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_font_size_small),
                        stringResource(id = R.string.title_font_size_medium),
                        stringResource(id = R.string.title_font_size_big)
                    )
                ),
                onItemSelected = {
                    when (it) {
                        0 -> {
                            onTextSizeChanged.invoke(JetPhonebookSize.Small)

                        }
                        1 -> {
                            onTextSizeChanged.invoke(JetPhonebookSize.Medium)
                        }
                        2 -> {
                            onTextSizeChanged.invoke(JetPhonebookSize.Big)
                        }
                    }
                }
            )
            CustomDivider()

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_padding_size),
                    currentIndex = when (currentPaddingSize) {
                        JetPhonebookSize.Small -> 0
                        JetPhonebookSize.Medium -> 1
                        JetPhonebookSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_padding_small),
                        stringResource(id = R.string.title_padding_medium),
                        stringResource(id = R.string.title_padding_big)
                    )
                ),
                onItemSelected = {
                    when (it) {
                        0 -> onPaddingSizeChanged.invoke(JetPhonebookSize.Small)
                        1 -> onPaddingSizeChanged.invoke(JetPhonebookSize.Medium)
                        2 -> onPaddingSizeChanged.invoke(JetPhonebookSize.Big)
                    }
                }
            )
            CustomDivider()

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_corners_style),
                    currentIndex = when (currentCornersStyle) {
                        JetPhonebookCorners.Rounded -> 0
                        JetPhonebookCorners.Flat -> 1
                    },
                    values = listOf(
                        stringResource(id = R.string.title_corners_style_rounded),
                        stringResource(id = R.string.title_corners_style_flat)
                    )
                ),
                onItemSelected = {
                    when (it) {
                        0 -> onCornersStyleChanged.invoke(JetPhonebookCorners.Rounded)
                        1 -> onCornersStyleChanged.invoke(JetPhonebookCorners.Flat)
                    }
                }
            )
            CustomDivider()


            SettingsView(settingsViewModel = SettingsViewModel(), activity = activity)
            Row(modifier= Modifier
                .fillMaxHeight()
                .padding(start = JetPhonebookTheme.shapes.padding, bottom = 60.dp)
                .weight(1f),
                verticalAlignment = Alignment.Bottom) {
                Text(
                    "Версия программы $versionName",
                    color = JetPhonebookTheme.colors.secondaryText,
                    fontSize = JetPhonebookTheme.typography.small.fontSize )
            }




           /* Row(
                modifier = Modifier
                    .padding(JetHabitTheme.shapes.padding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorCard(color = if (isDarkMode) purpleDarkPalette.tintColor else purpleLightPalette.tintColor,
                    onClick = {
                        onNewStyle.invoke(JetHabitStyle.Purple)
                    })
                ColorCard(color = if (isDarkMode) orangeDarkPalette.tintColor else orangeLightPalette.tintColor,
                    onClick = {
                        onNewStyle.invoke(JetHabitStyle.Orange)
                    })
                ColorCard(color = if (isDarkMode) blueDarkPalette.tintColor else blueLightPalette.tintColor,
                    onClick = {
                        onNewStyle.invoke(JetHabitStyle.Blue)
                    })
            }

            Row(
                modifier = Modifier
                    .padding(JetHabitTheme.shapes.padding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorCard(color = if (isDarkMode) redDarkPalette.tintColor else redLightPalette.tintColor,
                    onClick = {
                        onNewStyle.invoke(JetHabitStyle.Red)
                    })
                ColorCard(color = if (isDarkMode) greenDarkPalette.tintColor else greenLightPalette.tintColor,
                    onClick = {
                        onNewStyle.invoke(JetHabitStyle.Green)
                    })
            }*/

        }
    }






}

@Composable
fun ExitButton(navController: NavController, activity: Activity){
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs

    FloatingActionButton(onClick = {
        with (sharedPref.edit()) {
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
    //val expiresIn = sharedPref.getInt("expiresIn", 0) //достаем данные из shared prefs
   // val expiresOn = sharedPref.getInt("expiresOn", 0) //достаем данные из shared prefs
    val refreshToken = sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs



    val settings =   listOf(
        SettingsModel("Имя", "$firstName $lastName"),
        SettingsModel("Магазин", "$orgUnitName (${shopNumber})"),
       // SettingsModel("Должность", jobTitle),
        SettingsModel("Телефон", workPhone),
       // SettingsModel("refreshToken", refreshToken.toString())
    )

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

    ) {



            /* Text(model.title, modifier = Modifier.weight(0.3f), style = TextStyle(color= Color.Black))
        Text(model.value, modifier = Modifier
            .weight(0.7f)
            .padding(8.dp), style = TextStyle(color= Color.Black))*/
            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = JetPhonebookTheme.shapes.padding),
                text = model.title,
                style = JetPhonebookTheme.typography.body,
                color = JetPhonebookTheme.colors.primaryText
            )

            Text(
                modifier = Modifier
                    .weight(0.7f),
                text = model.value,
                style = JetPhonebookTheme.typography.body,
                color = JetPhonebookTheme.colors.secondaryText,
                textAlign = TextAlign.End
            )


        }
        Divider(
            modifier = Modifier.padding(
                start = JetPhonebookTheme.shapes.padding,
                end = JetPhonebookTheme.shapes.padding),
            thickness = 1.dp,
            color = JetPhonebookTheme.colors.secondaryText.copy(
                alpha = 0.3f
            )
        )
    }


}

@Composable
fun CustomDivider(){
    Divider(
        modifier = Modifier.padding(start = JetPhonebookTheme.shapes.padding,
            end = JetPhonebookTheme.shapes.padding),
        thickness = 1.dp,
        color = JetPhonebookTheme.colors.secondaryText.copy(
            alpha = 0.3f
        )
    )
}

@Composable
fun ExitButton2(navController: NavController, activity: Activity){
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    //val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs

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

@Composable
fun ColorCard(
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(60.dp)
            .clickable {
                onClick.invoke()
            },
        backgroundColor = color,
        elevation = 8.dp,
        shape = JetPhonebookTheme.shapes.cornersStyle
    ) { }
}
