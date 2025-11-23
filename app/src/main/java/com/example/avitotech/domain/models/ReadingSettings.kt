package com.example.avitotech.domain.models

data class ReadingSettings(
    val fontSize: FontSize = FontSize.MEDIUM,
    val lineSpacing: LineSpacing = LineSpacing.MEDIUM,
    val theme: ReaderTheme = ReaderTheme.SYSTEM
)

enum class FontSize {
    SMALL, MEDIUM, LARGE
}

enum class LineSpacing {
    COMPACT, MEDIUM, COMFORTABLE
}

enum class ReaderTheme {
    LIGHT, DARK, SYSTEM
}