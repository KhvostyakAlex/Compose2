package ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.sample

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.AutoCompleteBox
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.utils.AutoCompleteSearchBarTag
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.searchbar.TextSearchBar
import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.models.Person


@ExperimentalAnimationApi
@Composable
fun AutoCompleteObjectSample(persons: List<Person>) {
    AutoCompleteBox(
        items = persons,
        itemContent = { person ->
            PersonAutoCompleteItem(person)
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { person ->
            value = person.name
            filter(value)
            view.clearFocus()
        }

        TextSearchBar(
            modifier = Modifier.testTag(AutoCompleteSearchBarTag),
            value = value,
            label = "Search with objects",
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
               // isSearching = focusState == FocusState.Active
                isSearching = focusState.isFocused
            },
            onValueChanged = { query ->
                value = query
                filter(value)
            }
        )
    }
}

@Composable
fun PersonAutoCompleteItem(person: Person) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = person.name, style = MaterialTheme.typography.subtitle2)
       // Text(text = person.age.toString(), style = MaterialTheme.typography.subtitle2)
    }
}
