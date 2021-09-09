package ru.leroymerlin.internal.compose2

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import ru.leroymerlin.internal.compose2.ui.screens.FindDepartmentScreen
import ru.leroymerlin.internal.compose2.ui.screens.findusers.FindUsersScreen


typealias ComposableFun = @Composable () -> Unit

sealed class TabItem( var icon: Int, var title: String, var screen: ComposableFun) {

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindUsers : TabItem(R.drawable.ic_search_black, "Поиск по сотруднику",
        { FindUsersScreen(findUsersViewModel = CardsViewModel()) })
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindDepartment : TabItem(R.drawable.ic_search_black, "Поиск по подразделению",
        { FindDepartmentScreen(viewModel = CardsViewModel()) })

}