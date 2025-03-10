package ru.leroymerlin.internal.phonebook.ui.themes.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.leroymerlin.internal.phonebook.ui.themes.JetPhonebookTheme


@Composable
fun JetPhonebookButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = JetPhonebookTheme.colors.tintColor,
    onClick: () -> Unit,
    text: String? = null,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {}
) {
    Button(
        modifier = modifier.height(48.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = backgroundColor.copy(
                alpha = 0.3f
            )
        )
    ) {
        text?.let {
            Text(
                text = it,
                style = JetPhonebookTheme.typography.body,
                color = Color.White
            )
        } ?: content.invoke(this)
    }
}

