package com.example.avitotech.presentation.screens.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.avitotech.domain.models.FontSize
import com.example.avitotech.domain.models.LineSpacing
import com.example.avitotech.domain.models.ReaderTheme
import com.example.avitotech.domain.models.ReadingSettings

@Composable
fun BookContentView(
    content: String,
    bookFormat: String,
    settings: ReadingSettings,
    onScrollProgress: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val backgroundColor = when (settings.theme) {
        ReaderTheme.LIGHT  -> MaterialTheme.colorScheme.surface
        ReaderTheme.DARK   -> Color(0xFF121212)
        ReaderTheme.SYSTEM -> MaterialTheme.colorScheme.background
    }

    val textColor = when (settings.theme) {
        ReaderTheme.LIGHT  -> MaterialTheme.colorScheme.onSurface
        ReaderTheme.DARK   -> MaterialTheme.colorScheme.onSurface
        ReaderTheme.SYSTEM -> MaterialTheme.colorScheme.onBackground
    }

    LaunchedEffect(scrollState.value) {
        val maxValue = scrollState.maxValue.toFloat()
        if (maxValue > 0) {
            val progress = scrollState.value.toFloat() / maxValue
            onScrollProgress(progress)
        }
    }

    Box(
        modifier = modifier
            .background(backgroundColor)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = getFontSize(settings.fontSize),
                lineHeight = getLineHeight(settings.fontSize, settings.lineSpacing)
            ),
            color = textColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

fun getFontSize(fontSize: FontSize): TextUnit = when (fontSize) {
    FontSize.SMALL -> 14.sp
    FontSize.MEDIUM -> 16.sp
    FontSize.LARGE -> 18.sp
}

fun getLineHeight(fontSize: FontSize, lineSpacing: LineSpacing): TextUnit {
    val baseLineHeight = when (fontSize) {
        FontSize.SMALL -> 18.sp
        FontSize.MEDIUM -> 22.sp
        FontSize.LARGE -> 26.sp
    }

    return when (lineSpacing) {
        LineSpacing.COMPACT -> baseLineHeight
        LineSpacing.MEDIUM -> baseLineHeight * 1.3f
        LineSpacing.COMFORTABLE -> baseLineHeight * 1.6f
    }
}