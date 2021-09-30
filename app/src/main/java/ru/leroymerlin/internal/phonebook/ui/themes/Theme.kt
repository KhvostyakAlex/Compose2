package ru.leroymerlin.internal.phonebook.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.leroymerlin.internal.phonebook.R


@Composable
fun MainTheme(
    style: JetPhonebookStyle = JetPhonebookStyle.Purple,
    textSize: JetPhonebookSize = JetPhonebookSize.Medium,
    paddingSize: JetPhonebookSize = JetPhonebookSize.Medium,
    corners: JetPhonebookCorners = JetPhonebookCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                JetPhonebookStyle.Purple -> purpleDarkPalette
                JetPhonebookStyle.Blue -> blueDarkPalette
                JetPhonebookStyle.Orange -> orangeDarkPalette
                JetPhonebookStyle.Red -> redDarkPalette
                JetPhonebookStyle.Green -> greenDarkPalette
            }
        }
        false -> {
            when (style) {
                JetPhonebookStyle.Purple -> purpleLightPalette
                JetPhonebookStyle.Blue -> blueLightPalette
                JetPhonebookStyle.Orange -> orangeLightPalette
                JetPhonebookStyle.Red -> redLightPalette
                JetPhonebookStyle.Green -> greenLightPalette
            }
        }
    }

    val typography = JetPhonebookTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                JetPhonebookSize.Small -> 24.sp
                JetPhonebookSize.Medium -> 28.sp
                JetPhonebookSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                JetPhonebookSize.Small -> 14.sp
                JetPhonebookSize.Medium -> 16.sp
                JetPhonebookSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                JetPhonebookSize.Small -> 14.sp
                JetPhonebookSize.Medium -> 16.sp
                JetPhonebookSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                JetPhonebookSize.Small -> 10.sp
                JetPhonebookSize.Medium -> 12.sp
                JetPhonebookSize.Big -> 14.sp
            }
        )
        ,
        small = TextStyle(
            fontSize = when (textSize) {
                JetPhonebookSize.Small -> 8.sp
                JetPhonebookSize.Medium -> 10.sp
                JetPhonebookSize.Big -> 12.sp
            },
            fontWeight = FontWeight.Normal
        )
    )

    val shapes = JetPhonebookShape(
        padding = when (paddingSize) {
            JetPhonebookSize.Small -> 12.dp
            JetPhonebookSize.Medium -> 16.dp
            JetPhonebookSize.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            JetPhonebookCorners.Flat -> RoundedCornerShape(0.dp)
            JetPhonebookCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )


    CompositionLocalProvider(
        LocalJetPhonebookColors provides colors,
        LocalJetPhonebookTypography provides typography,
        LocalJetPhonebookShape provides shapes,
        content = content
    )
}