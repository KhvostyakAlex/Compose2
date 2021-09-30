package ru.leroymerlin.internal.phonebook.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class JetPhonebookColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val thirdText:Color,
    val tintColor: Color,
    val controlColor: Color,
    val errorColor: Color
)

data class JetPhonebookTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle,
    val small: TextStyle
)

data class JetPhonebookShape(
    val padding: Dp,
    val cornersStyle: Shape
)


object JetPhonebookTheme {
    val colors: JetPhonebookColors
        @Composable
        get() = LocalJetPhonebookColors.current

    val typography: JetPhonebookTypography
        @Composable
        get() = LocalJetPhonebookTypography.current

    val shapes: JetPhonebookShape
        @Composable
        get() = LocalJetPhonebookShape.current

}

enum class JetPhonebookStyle {
    Purple, Orange, Blue, Red, Green
}

enum class JetPhonebookSize {
    Small, Medium, Big
}

enum class JetPhonebookCorners {
    Flat, Rounded
}

val LocalJetPhonebookColors = staticCompositionLocalOf<JetPhonebookColors> {
    error("No colors provided")
}

val LocalJetPhonebookTypography = staticCompositionLocalOf<JetPhonebookTypography> {
    error("No font provided")
}

val LocalJetPhonebookShape = staticCompositionLocalOf<JetPhonebookShape> {
    error("No shapes provided")
}
