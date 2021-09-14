package ru.leroymerlin.internal.compose2.ui.screens.finddepartment

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.AutoCompleteBox
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.utils.AutoCompleteSearchBarTag
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.utils.asAutoCompleteEntities
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.searchbar.TextSearchBar

import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import java.util.*


@ExperimentalAnimationApi
@Composable
fun FindDepartmentScreen( viewModel: CardsViewModel){

    Scaffold {

        val bottomItems = listOf("list", "search", "push", "cards")

       val arrJobTitleList = listOf(
            "Все",
            "менеджер отдела",
            "продавец-консультант",
            "руководитель торгового сектора",
            "администратор цепи поставок магазина",
            "специалист по администрированию персонала",
            "менеджер сектора по обслуживанию клиентов",
            "специалист цепи поставок магазина",
            "менеджер по охране труда",
            "кассир-консультант",
            "менеджер цепи поставок магазина",
            "специалист технической поддержки",
            "механик",
            "менеджер по административным и бухгалтерским вопросам",
            "специалист по продажам",
            "инженер-энергетик",
            "техник-консультант",
            "Директор магазина",
            "руководитель сектора по обслуживанию клиентов",
            "руководитель цепи поставок магазина",
            "инженер-теплотехник",
            "контролер управления",
            "дизайнер"
        )

        Column {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
              //  AutoCompleteObjectSample(persons = persons)
                AutoCompleteValueSample(items = bottomItems, "Магазин", "Магазин Барнаул 1")
                AutoCompleteValueSample(items = arrJobTitleList, "Должность")
               // AutoCompleteValueSample(items = names)
               // Log.e("AutoCompleteValueSample", )
            }


        }

    }
}

@ExperimentalAnimationApi
@Composable
fun AutoCompleteValueSample(items: List<String>, label:String, defaultValue: String? = null) {



    val autoCompleteEntities = items.asAutoCompleteEntities(
        filter = { item, query ->
            item.toLowerCase(Locale.getDefault())
                .startsWith(query.toLowerCase(Locale.getDefault()))
        }
    )

    AutoCompleteBox(
        items = autoCompleteEntities,
        itemContent = { item ->
            ValueAutoCompleteItem(item.value)
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current
       /* if(defaultValue?.isNotEmpty() == true){
            value= defaultValue
        }*/
        onItemSelected { item ->
            value = item.value
            filter(value)
            view.clearFocus()
            Log.e("onItemSelected", value)
        }

        TextSearchBar(
            modifier = Modifier.testTag(AutoCompleteSearchBarTag),
            value = value ,
            label = label,
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
                //isSearching = focusState == FocusState.Active
                isSearching = focusState.isFocused
            }
        ) { query ->
            value = query
            filter(value)
        }
    }
}

@Composable
fun ValueAutoCompleteItem(item: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = item, style = MaterialTheme.typography.subtitle2)
    }
}