package ru.leroymerlin.internal.phonebook.ui.screens.finddepartment

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.navigation.NavController
import ru.leroymerlin.internal.phonebook.addToFB
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.ui.screens.autocompletetext.components.autocomplete.AutoCompleteBox
import ru.leroymerlin.internal.phonebook.ui.screens.autocompletetext.components.autocomplete.utils.AutoCompleteSearchBarTag
import ru.leroymerlin.internal.phonebook.ui.screens.autocompletetext.components.autocomplete.utils.asAutoCompleteEntities
import ru.leroymerlin.internal.phonebook.ui.screens.autocompletetext.components.searchbar.TextSearchBar
import ru.leroymerlin.internal.phonebook.ui.screens.cards.ExpandableCard
import ru.leroymerlin.internal.phonebook.ui.screens.findusers.FindUsersViewModel
import ru.leroymerlin.internal.phonebook.ui.themes.JetHabitTheme
import values.arrJobTitleList

import java.util.*

data class filter(val key:String, val value:String)
var filterData = mutableMapOf<String, String>()


@ExperimentalAnimationApi
@Composable
fun FindDepartmentScreen(findDepartmentViewModel: FindDepartmentViewModel, navController:NavController){
    val depData:List<String> by findDepartmentViewModel.depData.observeAsState(emptyList())
    val cards:List<IntraruUserDataList> by findDepartmentViewModel.cards.observeAsState(emptyList())
    val expandedCardIds = findDepartmentViewModel.expandedCardIdsList.observeAsState()
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val authHeader = sharedPref.getString("authHeader", "").toString() //достаем данные из shared prefs
    val orgInitNameUser = sharedPref.getString("orgUnitName", "").toString() //достаем данные из shared prefs
    val refreshToken = sharedPref.getString("refreshToken", "").toString() //достаем данные из shared prefs
    val account = sharedPref.getString("account", "").toString() //достаем данные из shared prefs
    Surface(
        color = JetHabitTheme.colors.primaryBackground
    ) {


        Scaffold {


            findDepartmentViewModel.getDepartment(authHeader = authHeader)
            Column(
                modifier = Modifier
                    .fillMaxSize(),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                if (depData.isNotEmpty()) {
                    AutoCompleteValueSample(
                        items = depData,
                        "Магазин",
                        orgInitNameUser,
                        orgInitNameUser,
                        "mag",
                        authHeader,
                        findDepartmentViewModel,
                        account
                    )
                } else {
                    // Log.e("depData","-empty")
                    //когда закончилось время токена и depData отсутствует
                    AutoCompleteValueSample(
                        items = listOf(""),
                        "Магазин",
                        "",
                        "",
                        "mag",
                        authHeader,
                        findDepartmentViewModel,
                        account
                    )


                }

                AutoCompleteValueSample(
                    items = arrJobTitleList,
                    "Должность",
                    "Все",
                    orgInitNameUser,
                    "jobTitle",
                    authHeader,
                    findDepartmentViewModel,
                    account
                )

                LaunchedEffect(Unit) {
                    findDepartmentViewModel.getUserByDepartment(
                        orgUnitName = orgInitNameUser,
                        jobTitle = "Все",
                        authHeader = authHeader
                    )
                }

                if (cards.isNotEmpty()) {
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
}


@ExperimentalAnimationApi
@Composable
fun AutoCompleteValueSample(items: List<String>,
                            label:String,
                            defaultValue: String? = null,
                            orgUnitNameUser: String? = null,
                            type:String,
                            authHeader: String,
                            findDepartmentViewModel: FindDepartmentViewModel,
                            account:String) {

    val autoCompleteEntities = items.asAutoCompleteEntities(
        filter = { item, query ->
            item.lowercase(Locale.getDefault())
                .startsWith(query.lowercase(Locale.getDefault()))
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

            var orgUnitName:String
            var jobTitle: String
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
                //добавляем в аналитику
                addToFB("FindDepatrment", account, orgUnitName+"_"+jobTitle)
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

@Composable
fun refreshToken(activity: Activity,
                 findDepartmentViewModel: FindDepartmentViewModel,
                 refreshToken:String,
                 //navController: NavController
) {
    val tokenData:List<IntraruAuthUserData> by findDepartmentViewModel.tokenData.observeAsState(emptyList())
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    findDepartmentViewModel.refreshToken(refreshToken)
    if(tokenData.isNotEmpty()){
        //Log.e("tokenData - ", tokenData.toString())
        val t = tokenData[0]

        with (sharedPref.edit()) {
            putString("token", t.token)
            putString("refreshToken", t.refreshToken)
            putInt("expiresIn", t.expiresIn)
            putInt("expiresOn", t.expiresOn)
            putString("authHeader", "Bearer " + t.token)
            apply()
        }
        Toast.makeText(activity, "Перезаходим в приложение, попробуй еще раз.", Toast.LENGTH_SHORT).show()
       /* navController.navigate("search") {
            popUpTo("search") {
                inclusive = true
            }
        }*/
    }
}
