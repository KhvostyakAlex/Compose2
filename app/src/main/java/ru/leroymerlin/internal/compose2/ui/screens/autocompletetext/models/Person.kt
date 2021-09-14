package ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.models

import ru.leroymerlin.internal.compose2.ui.screens.autocompletetext.components.autocomplete.AutoCompleteEntity
import java.util.Locale

data class Person(
    val name: String,
    val age: Int
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean {
        return name.toLowerCase(Locale.getDefault())
            .startsWith(query.toLowerCase(Locale.getDefault()))
    }
}
