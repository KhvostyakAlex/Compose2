package ru.leroymerlin.internal.phonebook.ui.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.phonebook.ui.screens.finddepartment.FindDepartmentScreen
import ru.leroymerlin.internal.phonebook.ui.screens.finddepartment.FindDepartmentViewModel
import ru.leroymerlin.internal.phonebook.ui.screens.findusers.FindUsersScreen
import ru.leroymerlin.internal.phonebook.ui.screens.findusers.FindUsersViewModel


typealias ComposableFun = @Composable () -> Unit
sealed class TabItem( var icon: Int, var title: String, var screen: ComposableFun) {
}
lateinit var navControl:NavController

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SearchScreen(searchViewModel: SearchViewModel, navController: NavController) {
    val connect:List<String> by searchViewModel.connect.observeAsState(emptyList())
    val tokenData:List<IntraruAuthUserList> by searchViewModel.tokenData.observeAsState(emptyList())
    navControl = navController

    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val authHeader = sharedPref.getString("authHeader", "").toString() //достаем данные из shared prefs
    val orgInitNameUser = sharedPref.getString("orgUnitName", "").toString() //достаем данные из shared prefs
    val refreshToken = sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs
    val token = sharedPref.getString("token", "").toString() //достаем данные из shared prefs

    val tabs = listOf(FindUsers,
        FindDepartment)
    val pagerState = rememberPagerState(pageCount = tabs.size)
    Scaffold(
       // topBar = { TopBar() },
    ) {
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


        if(connect.isEmpty()){
            Log.e("searchScreen connect", "empty!")
            searchViewModel.refreshToken(refreshToken)
        }


        if (tokenData.isNotEmpty()) {
            Log.e("tokenData - ", tokenData[0].message)
            if(tokenData[0].message =="Success"){
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
            }else{
               // searchViewModel.refreshToken(refreshToken)
            }
        }




        Column {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
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
    { FindUsersScreen(findUsersViewModel = FindUsersViewModel(), navController = navControl)})
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
        backgroundColor = Color.White,
        contentColor = colorResource(id = R.color.black),
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
                colorResource(id = R.color.lmNCKD)
            } else {
                colorResource(id = R.color.colorGrey)
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
