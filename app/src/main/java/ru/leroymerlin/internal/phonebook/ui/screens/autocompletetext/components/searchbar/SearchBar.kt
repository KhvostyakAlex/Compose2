package ru.leroymerlin.internal.phonebook.ui.screens.autocompletetext.components.searchbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.leroymerlin.internal.phonebook.ui.themes.JetHabitTheme

@Composable
fun TextSearchBar(
    modifier: Modifier = Modifier,
    //value: String,
    value: String,
    label: String,
    onDoneActionClick: () -> Unit = {},

    onClearClick: () -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(1f)
            .onFocusChanged { onFocusChanged(it) },
        shape = RoundedCornerShape(8.dp),
        value = value,
        onValueChange = { query ->
            onValueChanged(query)
        },
        label = { Text(text = label) },
      //  label = { Text(text = defaultValue) },
        //textStyle = MaterialTheme.typography.subtitle1,
        textStyle = JetHabitTheme.typography.body,
       // textStyle = MaterialTheme.shapes.small.bottomStart.,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onClearClick() }) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
            }
        },
        keyboardActions = KeyboardActions(onDone = { onDoneActionClick() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}
