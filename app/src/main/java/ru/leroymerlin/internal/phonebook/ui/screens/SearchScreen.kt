package ru.leroymerlin.internal.phonebook.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import ru.leroymerlin.internal.phonebook.R
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
fun SearchScreen(navController: NavController) {
    navControl = navController
    val tabs = listOf(FindUsers,
        FindDepartment)
    val pagerState = rememberPagerState(pageCount = tabs.size)
    Scaffold(
       // topBar = { TopBar() },
    ) {


        Column {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}



@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
object FindUsers : TabItem(R.drawable.ic_action_user_black, "Поиск по сотруднику",
    { FindUsersScreen(findUsersViewModel = FindUsersViewModel(), navController = navControl)})
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
object FindDepartment : TabItem(R.drawable.ic_action_store_black, "Поиск по подразделению",
    { FindDepartmentScreen(findDepartmentViewModel = FindDepartmentViewModel()) })



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
