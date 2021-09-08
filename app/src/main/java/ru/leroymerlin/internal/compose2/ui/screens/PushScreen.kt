package ru.leroymerlin.internal.compose2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import ru.leroymerlin.internal.compose2.TabItem



@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun PushScreen() {
    val tabs = listOf(TabItem.FindUsers, TabItem.FindDepartment)
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