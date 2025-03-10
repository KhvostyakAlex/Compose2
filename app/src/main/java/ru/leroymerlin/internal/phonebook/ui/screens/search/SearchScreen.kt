package ru.leroymerlin.internal.phonebook.ui.screens.search

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.phonebook.ui.screens.finddepartment.FindDepartmentScreen
import ru.leroymerlin.internal.phonebook.ui.screens.finddepartment.FindDepartmentViewModel
import ru.leroymerlin.internal.phonebook.ui.screens.findusers.FindUsersScreen
import ru.leroymerlin.internal.phonebook.ui.screens.findusers.FindUsersViewModel
import ru.leroymerlin.internal.phonebook.ui.themes.JetPhonebookTheme


typealias ComposableFun = @Composable () -> Unit
sealed class TabItem( var icon: Int, var title: String, var screen: ComposableFun) {
}
lateinit var navControl:NavController

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SearchScreen(searchViewModel: SearchViewModel, navController: NavController, modifier: Modifier) {
    val connect: List<String> by searchViewModel.connect.observeAsState(emptyList())
    val tokenData: List<IntraruAuthUserList> by searchViewModel.tokenData.observeAsState(emptyList())
    navControl = navController

    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val authHeader =
        sharedPref.getString("authHeader", "").toString() //достаем данные из shared prefs
    val orgInitNameUser =
        sharedPref.getString("orgUnitName", "").toString() //достаем данные из shared prefs
    val refreshToken =
        sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs
    val token = sharedPref.getString("token", "").toString() //достаем данные из shared prefs
    val signin = sharedPref.getBoolean("signin?", false)
    val tabs = listOf(
        FindUsers,
        FindDepartment
    )
    val pagerState = rememberPagerState(pageCount = tabs.size)

    Surface(
        modifier = modifier,
       // color = JetHabitTheme.colors.primaryBackground,
        color = JetPhonebookTheme.colors.thirdText,

    ) {
            Scaffold(
                backgroundColor = JetPhonebookTheme.colors.secondaryBackground
            ) {

                //при запуске проверяем злогинен ли пользователь?
                LaunchedEffect(Unit) {
                    if (!signin) {
                        navController.popBackStack()
                        //  loginViewModel.authIntraru("login", "password")
                        navController.navigate("login") {
                            popUpTo = 0
                        }
                    }
                }

                //запрашиваем департмент, чтобы проверить связь
                searchViewModel.getConnect(authHeader = authHeader)

        /*
                if(depData.isNotEmpty()){
                    Log.e("searchScren depData", "depdta - ")
                }else {
                    Log.e("searchScren depData", "empty!")
                    //refreshToken(activity, searchViewModel, refreshToken,navController)
                    searchViewModel.refreshToken(refreshToken)
                }

         */

                if (connect.isEmpty()) {
                    val comment = "отсутствует соединение, запрашиваю access token через refreshToken"
                    Log.e("searchScreen", comment)
                    searchViewModel.refreshToken(refreshToken)
                }

                if (tokenData.isNotEmpty()) {
                    Log.e("tokenData - ", tokenData[0].message)
                    if (tokenData[0].message == "Success") {
                        val t = tokenData[0].IntraruAuthUserData
                        Log.e("refreshToken", t.refreshToken)
                        with(sharedPref.edit()) {
                            putString("token", t.token)
                            putString("refreshToken", t.refreshToken)
                            putInt("expiresIn", t.expiresIn)
                            putInt("expiresOn", t.expiresOn)
                            putString("authHeader", "Bearer " + t.token)
                            apply()
                        }


                        /* Toast.makeText(
                             activity,
                             "Перезаходим в приложение, попробуй еще раз.",
                             Toast.LENGTH_SHORT
                         ).show()
                         navController.navigate("search") {
                             popUpTo("search") {
                                 inclusive = true
                             }
                         }

                          */
                    }
                } else {
                    Log.e("searchScreen-", "tokenData- empty")

                    /* navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }

                    */
                }




                Column {
                    TopAppBar(
                        backgroundColor = JetPhonebookTheme.colors.primaryBackground,
                        elevation = 8.dp
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = JetPhonebookTheme.shapes.padding),
                            text = stringResource(id = R.string.app_name),
                            color = JetPhonebookTheme.colors.primaryText,
                            style = JetPhonebookTheme.typography.toolbar
                        )
                    }

                    Tabs(tabs = tabs, pagerState = pagerState)
                    TabsContent(tabs = tabs, pagerState = pagerState)
                }
            }
    }
}
/*@Composable
fun refreshToken(activity: Activity,
                 searchViewModel: SearchViewModel,
                 refreshToken:String,
                 navController: NavController) {
    val tokenData:List<IntraruAuthUserList> by searchViewModel.tokenData.observeAsState(emptyList())
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    searchViewModel.refreshToken(refreshToken)
    if(tokenData.isNotEmpty()){
        Log.e("tokenData - ", tokenData.toString())
        val t = tokenData[0].IntraruAuthUserData

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
*/

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
object FindUsers : TabItem(R.drawable.ic_action_user_black, "Поиск по сотруднику",
    { FindUsersScreen(findUsersViewModel = FindUsersViewModel(), navController = navControl, modifier = Modifier)})
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
object FindDepartment : TabItem(R.drawable.ic_action_store_black, "Поиск по подразделению",
    { FindDepartmentScreen(findDepartmentViewModel = FindDepartmentViewModel(), navController = navControl) })



@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    TabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        // backgroundColor = colorResource(id = R.color.lmNCKD),
        //backgroundColor = Color.White,
        backgroundColor = JetPhonebookTheme.colors.primaryBackground,
       // contentColor = colorResource(id = R.color.black),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions),
                color = colorResource(id = R.color.lmNCKD)


            )
        }) {
        // Add tabs for all of our pages
        tabs.forEachIndexed { index, tab ->
            val selectedTabsColor = if (pagerState.currentPage == index) {
                JetPhonebookTheme.colors.thirdText
            } else {
                JetPhonebookTheme.colors.controlColor
                //colorResource(id = R.color.colorGrey)
            }

            LeadingIconTab(
                icon = {
                    Icon(painter = painterResource(id = tab.icon),
                        contentDescription = "",
                        tint = selectedTabsColor)},
                text = { Text(tab.title, color= selectedTabsColor) },

                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}


@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        tabs[page].screen()
    }
}
