package ru.leroymerlin.internal.compose2.ui.screens.finddepartment

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.AutoCompleteBox
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.utils.AutoCompleteSearchBarTag
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.utils.asAutoCompleteEntities
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.searchbar.TextSearchBar
import ru.leroymerlin.internal.compose2.ui.screens.cards.ExpandableCard

import java.util.*
import kotlin.collections.ArrayList

data class filter(val key:String, val value:String)
var filterData = mutableMapOf<String, String>()

@ExperimentalAnimationApi
@Composable
fun FindDepartmentScreen(findDepartmentViewModel: FindDepartmentViewModel){
    val depData:List<String> by findDepartmentViewModel.depData.observeAsState(emptyList())
    val userData:List<IntraruUserDataList> by findDepartmentViewModel.userData.observeAsState(emptyList())
    val cards:List<IntraruUserDataList> by findDepartmentViewModel.cards.observeAsState(emptyList())
    val expandedCardIds = findDepartmentViewModel.expandedCardIdsList.observeAsState()
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
   // val filterData = ArrayList<filter>()

    //var filterData by remember { mutableStateOf("") }
    val authHeader = sharedPref.getString("authHeader", "").toString() //достаем данные из shared prefs
    val orgInitNameUser = sharedPref.getString("orgUnitName", "").toString() //достаем данные из shared prefs
    Scaffold {

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

        findDepartmentViewModel.getDepartment(authHeader = authHeader.toString())

            Column(
                modifier = Modifier
                    .fillMaxSize(),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
              //  AutoCompleteObjectSample(persons = persons)

                if(depData.isNotEmpty()){
                    AutoCompleteValueSample(items = depData, "Магазин", orgInitNameUser, orgInitNameUser,"mag", authHeader, findDepartmentViewModel)
                }

                AutoCompleteValueSample(items = arrJobTitleList, "Должность", "Все", orgInitNameUser,"jobTitle", authHeader, findDepartmentViewModel)
               // AutoCompleteValueSample(items = names)
               // Log.e("AutoCompleteValueSample", )

                LaunchedEffect(Unit){
                    findDepartmentViewModel.getUserByDepartment(
                        orgUnitName = orgInitNameUser,
                        jobTitle = "Все",
                        authHeader = authHeader)
                }

                if(cards.isNotEmpty()){
                    Log.e("cards", cards.toString())
                    LazyColumn {
                        itemsIndexed(cards) { _, card ->
                            ExpandableCard(
                                card = card,
                                onCardArrowClick = { findDepartmentViewModel.onCardArrowClicked(card.account.toInt()) },
                                expanded = expandedCardIds.value!!.contains(card.account.toInt()),
                            )
                        }
                    }
                }
            }
    }
}


@ExperimentalAnimationApi
@Composable
fun AutoCompleteValueSample(items: List<String>,
                            label:String,
                            defaultValue: String? = null,
                            orgUnitNameUser: String? = null,
                            type:String,
                            authHeader: String,
findDepartmentViewModel: FindDepartmentViewModel) {

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

        onItemSelected { item ->
            value = item.value
            filter(value)
            view.clearFocus()
            filterData.put(type, value)
          //  Log.e("filterData -", filterData.toString())
            // Log.e("val - ", "job-"+ filterData["jobTitle"].toString())

            var orgUnitName = ""
            var jobTitle =""
            orgUnitName = filterData["mag"].toString()
            jobTitle = filterData["jobTitle"].toString()

            if(orgUnitName == "null"){
                orgUnitName = orgUnitNameUser.toString()
            }
            if(jobTitle == "null"){
                jobTitle = "Все"
            }

            if (orgUnitName.isNotEmpty()) {
                if(jobTitle.isEmpty()){
                    jobTitle= "Все"
                }
                findDepartmentViewModel.getUserByDepartment(
                    orgUnitName = orgUnitName,
                    jobTitle = jobTitle,
                    authHeader = authHeader
                )
                // updateData(type, value, findDepartmentViewModel = FindDepartmentViewModel(), authHeader = authHeader)
            }
        }

        LaunchedEffect(Unit){

            if(defaultValue?.isNotEmpty() == true){
                value = defaultValue.toString()
            }
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


fun updateData(type:String, value:String, findDepartmentViewModel:FindDepartmentViewModel, authHeader:String) {
     filterData.put(type,value)
    Log.e("filterData -", filterData.toString())
   // Log.e("val - ", "job-"+ filterData["jobTitle"].toString())



    if(filterData["mag"]?.isNotEmpty() == true){
findDepartmentViewModel.getUserByDepartment(orgUnitName= filterData["mag"].toString(),
             jobTitle = filterData["jobTitle"].toString(),
             authHeader = authHeader)
        }

//Log.e("d-", userData.toString())

}