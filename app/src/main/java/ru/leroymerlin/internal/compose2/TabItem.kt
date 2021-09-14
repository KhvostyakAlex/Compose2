package ru.leroymerlin.internal.compose2

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel

import ru.leroymerlin.internal.compose2.ui.screens.findusers.FindUsersScreen

/*
typealias ComposableFun = @Composable () -> Unit


sealed class TabItem2( var icon: Int, var title: String, var screen: ComposableFun) {



    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindUsers : TabItem2(R.drawable.ic_search_black, "Поиск по сотруднику",
        { FindUsersScreen(findUsersViewModel = CardsViewModel() )})
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindDepartment : TabItem2(R.drawable.ic_search_black, "Поиск по подразделению",
        { FindDepartmentScreen(viewModel = CardsViewModel()) })

}*/