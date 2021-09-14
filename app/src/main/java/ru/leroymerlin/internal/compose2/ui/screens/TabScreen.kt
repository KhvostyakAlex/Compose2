package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.*

import kotlinx.coroutines.launch
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.ui.screens.findusers.FindUsersScreen
/*
typealias ComposableFun = @Composable () -> Unit
sealed class TabItem( var icon: Int, var title: String, var screen: ComposableFun) {

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindUsers : TabItem(R.drawable.ic_search_black, "Поиск по сотруднику",
        { FindUsersScreen(findUsersViewModel = CardsViewModel() )})
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindDepartment : TabItem(R.drawable.ic_search_black, "Поиск по подразделению",
        { FindDepartmentScreen(viewModel = CardsViewModel()) })

}


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
        contentColor = colorResource(id = R.color.lmNCKD),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        // Add tabs for all of our pages
        tabs.forEachIndexed { index, tab ->
            // OR Tab()
            LeadingIconTab(
                icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = "") },
                text = { Text(tab.title) },
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

/*
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val tabs = listOf(
        TabItem.FindUsers,
        TabItem.FindDepartment
    )
    val pagerState = rememberPagerState(pageCount = tabs.size)
    Tabs(tabs = tabs, pagerState = pagerState)
}*/

@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        tabs[page].screen()
    }
}



/*
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun TabsContentPreview() {
    val tabs = listOf(
        TabItem.FindUsers,
        TabItem.FindDepartment
    )
    val pagerState = rememberPagerState(pageCount = tabs.size)
    TabsContent(tabs = tabs, pagerState = pagerState)
}*/