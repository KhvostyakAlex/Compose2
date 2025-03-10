package ru.leroymerlin.internal.phonebook.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.ui.themes.JetPhonebookTheme
import ru.leroymerlin.internal.phonebook.ui.themes.MainTheme


data class MenuItemModel(
    val title: String,
    val currentIndex: Int = 0,
    val values: List<String>
)

@Composable
fun MenuItem(
    model: MenuItemModel,
    onItemSelected: ((Int) -> Unit)? = null
) {
    val isDropdownOpen = remember { mutableStateOf(false) }
    val currentPosition = remember { mutableStateOf(model.currentIndex) }

    Box(
        modifier = Modifier
            .background(JetPhonebookTheme.colors.secondaryBackground)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .clickable {
                    isDropdownOpen.value = true
                }
                .padding(JetPhonebookTheme.shapes.padding)
                .background(JetPhonebookTheme.colors.secondaryBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = JetPhonebookTheme.shapes.padding),
                text = model.title,
                style = JetPhonebookTheme.typography.body,
                color = JetPhonebookTheme.colors.primaryText
            )

            Text(
                text = model.values[currentPosition.value],
                style = JetPhonebookTheme.typography.body,
                color = JetPhonebookTheme.colors.secondaryText
            )

            Icon(
                modifier = Modifier
                    .padding(start = JetPhonebookTheme.shapes.padding / 4)
                    .size(18.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
                contentDescription = "Arrow",
                tint = JetPhonebookTheme.colors.secondaryText
            )
        }

        DropdownMenu(
            expanded = isDropdownOpen.value,
            onDismissRequest = {
                isDropdownOpen.value = false
            },
            modifier = Modifier.fillMaxWidth().background(JetPhonebookTheme.colors.primaryBackground)
        ) {
            model.values.forEachIndexed { index, value ->
                DropdownMenuItem(onClick = {
                    currentPosition.value = index
                    isDropdownOpen.value = false
                    onItemSelected?.invoke(index)
                }) {
                    Text(
                        text = value,
                        style = JetPhonebookTheme.typography.body,
                        color = JetPhonebookTheme.colors.primaryText
                    )
                }
            }
        }

      /*  Divider(
            modifier = Modifier.padding(start = JetHabitTheme.shapes.padding),
            thickness = 1.dp,
            color = JetHabitTheme.colors.secondaryText.copy(
                alpha = 0.5f
            )
        )

       */
    }
}

@Composable
@Preview
fun MenuItem_Preview() {
    MainTheme(
        darkTheme = true
    ) {
        MenuItem(
            model = MenuItemModel(
                title = stringResource(id = R.string.title_font_size),
                values = listOf(
                    stringResource(id = R.string.title_font_size_small),
                    stringResource(id = R.string.title_font_size_medium),
                    stringResource(id = R.string.title_font_size_big)
                )
            )
        )
    }
}

