package ru.leroymerlin.internal.compose2.ui.screens.finddepartment

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.ui.screens.AutoCompleteText
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.models.Person
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.sample.AutoCompleteObjectSample
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.sample.AutoCompleteValueSample
import ru.leroymerlin.internal.compose2.ui.screens.cards.ExpandableCard


@ExperimentalAnimationApi
@Composable
fun FindDepartmentScreen( viewModel: CardsViewModel){

    Scaffold {
            val textState = remember { mutableStateOf("")}
            val isEnabled = remember { mutableStateOf(false)}
            val isCompleteLogin = remember { mutableStateOf(0)}

        val bottomItems = listOf("list", "search", "push", "cards")
        val textStateLogin = remember { mutableStateOf("") }


        Column {

            val persons = listOf(
                Person(
                    name = "Paulo Pereira",
                    age = 23
                ),
                Person(
                    name = "Daenerys Targaryen",
                    age = 24
                ),
                Person(
                    name = "Jon Snow",
                    age = 24
                ),
                Person(
                    name = "Sansa Stark",
                    age = 20
                ),
            )
            val names = persons.map { it.name }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AutoCompleteObjectSample(persons = persons)
                AutoCompleteValueSample(items = names)
            }


        }

    }
}