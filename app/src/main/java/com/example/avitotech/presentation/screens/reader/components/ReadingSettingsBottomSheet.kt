package com.example.avitotech.presentation.screens.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.avitotech.R
import com.example.avitotech.domain.models.FontSize
import com.example.avitotech.domain.models.LineSpacing
import com.example.avitotech.domain.models.ReaderTheme
import com.example.avitotech.domain.models.ReadingSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingSettingsBottomSheet(
    settings: ReadingSettings,
    onSettingsChange: (ReadingSettings) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.setting_read),
                style = MaterialTheme.typography.titleLarge
            )

            SettingsSection(title = stringResource(R.string.font_size)) {
                FontSizeSelector(
                    currentSize = settings.fontSize,
                    onSizeSelected = { newSize ->
                        onSettingsChange(settings.copy(fontSize = newSize))
                    }
                )
            }

            SettingsSection(title = stringResource(R.string.line_spacing)) {
                LineSpacingSelector(
                    currentSpacing = settings.lineSpacing,
                    onSpacingSelected = { newSpacing ->
                        onSettingsChange(settings.copy(lineSpacing = newSpacing))
                    }
                )
            }

            SettingsSection(title = stringResource(R.string.topic)) {
                ThemeSelector(
                    currentTheme = settings.theme,
                    onThemeSelected = { newTheme ->
                        onSettingsChange(settings.copy(theme = newTheme))
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        content()
    }
}

@Composable
private fun FontSizeSelector(
    currentSize: FontSize,
    onSizeSelected: (FontSize) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FontSize.values().forEach { size ->
            Text(
                text = when (size) {
                    FontSize.SMALL  -> "Аа"
                    FontSize.MEDIUM -> "Аа"
                    FontSize.LARGE  -> "Аа"
                },
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = when (size) {
                        FontSize.SMALL  -> 14.sp
                        FontSize.MEDIUM -> 18.sp
                        FontSize.LARGE  -> 22.sp
                    }
                ),
                modifier = Modifier
                    .clickable { onSizeSelected(size) }
                    .padding(8.dp)
                    .background(
                        color = if (currentSize == size) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun LineSpacingSelector(
    currentSpacing: LineSpacing,
    onSpacingSelected: (LineSpacing) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LineSpacing.values().forEach { spacing ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSpacingSelected(spacing) }
                    .padding(8.dp)
                    .background(
                        color = if (currentSpacing == spacing) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(20.dp)
                ) {
                    when (spacing) {
                        LineSpacing.COMPACT -> {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .offset(y = (index * 4).dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                        LineSpacing.MEDIUM      -> {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .offset(y = (index * 6).dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                        LineSpacing.COMFORTABLE -> {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .offset(y = (index * 8).dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = when (spacing) {
                        LineSpacing.COMPACT     -> stringResource(R.string.narrow)
                        LineSpacing.MEDIUM      -> stringResource(R.string.average)
                        LineSpacing.COMFORTABLE -> stringResource(R.string.wide)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: ReaderTheme,
    onThemeSelected: (ReaderTheme) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ReaderTheme.values().forEach { theme ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onThemeSelected(theme) }
                    .padding(8.dp)
                    .background(
                        color = if (currentTheme == theme) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = when (theme) {
                                ReaderTheme.LIGHT  -> Color.White
                                ReaderTheme.DARK   -> Color.Black
                                ReaderTheme.SYSTEM -> Color.Gray
                            },
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = CircleShape
                        )
                )
                Text(
                    text = when (theme) {
                        ReaderTheme.LIGHT  -> stringResource(R.string.light)
                        ReaderTheme.DARK   -> stringResource(R.string.dark)
                        ReaderTheme.SYSTEM -> stringResource(R.string.system)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}