package ru.leroymerlin.internal.phonebook.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties

    @Composable
    fun AutoCompleteText(
        value: String,
        onValueChange: (String) -> Unit,
        onOptionSelected: (String) -> Unit,
        modifier: Modifier = Modifier,
        label: @Composable (() -> Unit)? = null,
        suggestions: List<String> = emptyList()
    ) {
        Column(modifier = modifier) {
            OutlinedTextField(
                value = value,
                onValueChange = { text -> if (text !== value) onValueChange(text) },
                modifier = Modifier.fillMaxWidth(),
                label = label,
            )
            DropdownMenu(
                expanded = suggestions.isNotEmpty(),
                onDismissRequest = {  },
                modifier = Modifier.fillMaxWidth(),
                // This line here will accomplish what you want
                properties = PopupProperties(focusable = false)
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(label)
                    }) {
                        Text(text = label)
                    }
                }
            }
        }
    }
