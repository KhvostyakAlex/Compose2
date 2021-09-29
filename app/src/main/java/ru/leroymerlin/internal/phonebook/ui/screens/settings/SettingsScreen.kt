package ru.leroymerlin.internal.phonebook.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    currentTextSize: JetHabitSize,
    currentPaddingSize: JetHabitSize,
    currentCornersStyle: JetHabitCorners,
    onDarkModeChanged: (Boolean) -> Unit,
    onNewStyle: (JetHabitStyle) -> Unit,
    onTextSizeChanged: (JetHabitSize) -> Unit,
    onPaddingSizeChanged: (JetHabitSize) -> Unit,
    onCornersStyleChanged: (JetHabitCorners) -> Unit,

    ) {
    val activity = LocalContext.current as Activity
    val versionName: String = BuildConfig.VERSION_NAME
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
            Divider()
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
        color = JetHabitTheme.colors.secondaryBackground,
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(
                backgroundColor = JetHabitTheme.colors.primaryBackground,
                elevation = 8.dp
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = JetHabitTheme.shapes.padding),
                    text = stringResource(id = R.string.title_settings),
                    color = JetHabitTheme.colors.primaryText,
                    style = JetHabitTheme.typography.toolbar
                )
            }

            Row(
                modifier = Modifier.padding(JetHabitTheme.shapes.padding)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.action_dark_theme_enable),
                    color = JetHabitTheme.colors.primaryText,
                    style = JetHabitTheme.typography.body
                )
                Checkbox(
                    checked = isDarkMode, onCheckedChange = {
                        onDarkModeChanged.invoke(it)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = JetHabitTheme.colors.thirdText,
                        uncheckedColor = JetHabitTheme.colors.secondaryText
                    )
                )
            }

            Divider(
                modifier = Modifier.padding(start = JetHabitTheme.shapes.padding),
                thickness = 0.5.dp,
                color = JetHabitTheme.colors.secondaryText.copy(
                    alpha = 0.3f
                )
            )

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_font_size),
                    currentIndex = when (currentTextSize) {
                        JetHabitSize.Small -> 0
                        JetHabitSize.Medium -> 1
                        JetHabitSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_font_size_small),
                        stringResource(id = R.string.title_font_size_medium),
                        stringResource(id = R.string.title_font_size_big)
                    )
                ),
                onItemSelected = {
                    when (it) {
                        0 -> onTextSizeChanged.invoke(JetHabitSize.Small)
                        1 -> onTextSizeChanged.invoke(JetHabitSize.Medium)
                        2 -> onTextSizeChanged.invoke(JetHabitSize.Big)
                    }
                }
            )

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_padding_size),
                    currentIndex = when (currentPaddingSize) {
                        JetHabitSize.Small -> 0
                        JetHabitSize.Medium -> 1
                        JetHabitSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_padding_small),
                        stringResource(id = R.string.title_padding_medium),
                        stringResource(id = R.string.title_padding_big)
                    )
                ),
                onItemSelected = {
                    when (it) {
                        0 -> onPaddingSizeChanged.invoke(JetHabitSize.Small)
                        1 -> onPaddingSizeChanged.invoke(JetHabitSize.Medium)
                        2 -> onPaddingSizeChanged.invoke(JetHabitSize.Big)
                    }
                }
            )

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_corners_style),
                    currentIndex = when (currentCornersStyle) {
                        JetHabitCorners.Rounded -> 0
                        JetHabitCorners.Flat -> 1
                    },
                    values = listOf(
                        stringResource(id = R.string.title_corners_style_rounded),
                        stringResource(id = R.string.title_corners_style_flat)
                    )
                ),
                onItemSelected = {
                    when (it) {
                        0 -> onCornersStyleChanged.invoke(JetHabitCorners.Rounded)
                        1 -> onCornersStyleChanged.invoke(JetHabitCorners.Flat)
                    }
                }
            )

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
/*
            HabbitCardItem(
                model = HabbitCardItemModel(
                    habbitId = 0,
                    title = "Пример карточки",
                    isChecked = true
                )
            )

 */
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
        shape = JetHabitTheme.shapes.cornersStyle
    ) { }
}
