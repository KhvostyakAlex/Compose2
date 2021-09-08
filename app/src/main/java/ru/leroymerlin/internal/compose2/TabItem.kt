package ru.leroymerlin.internal.compose2

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import ru.leroymerlin.internal.compose2.ui.screens.FindDepartmentScreen
import ru.leroymerlin.internal.compose2.ui.screens.FindUsersScreen



typealias ComposableFun = @Composable () -> Unit

sealed class TabItem( var icon: Int, var title: String, var screen: ComposableFun) {
val cardsViewModel:ViewModel = CardsViewModel()

    //object Music : TabItem(R.drawable.ic_expand_less_24, "Поиск 1", { SearchScreen((navController, cardsViewModel) })
   // object Search : TabItem(R.drawable.ic_expand_less_24, "Поиск 1", { SearchScreen(ca) })
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindUsers : TabItem(R.drawable.ic_search_black, "Поиск по сотруднику",
        { FindUsersScreen(viewModel = CardsViewModel()) })
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    object FindDepartment : TabItem(R.drawable.ic_search_black, "Поиск по подразделению",
        { FindDepartmentScreen(viewModel = CardsViewModel()
    ) })
  //  object Movies : TabItem(R.drawable.ic_expand_more_24, "Поиск 2", { FindDepartmentScreen((navController, cardsViewModel) })
}