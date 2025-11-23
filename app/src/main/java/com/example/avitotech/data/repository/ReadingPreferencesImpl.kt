package com.example.avitotech.data.repository

import android.content.Context
import com.example.avitotech.domain.models.FontSize
import com.example.avitotech.domain.models.LineSpacing
import com.example.avitotech.domain.models.ReaderTheme
import com.example.avitotech.domain.models.ReadingSettings
import com.example.avitotech.domain.repository.ReadingPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadingPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ReadingPreferences {

    private val sharedPreferences = context.getSharedPreferences("reading_prefs", Context.MODE_PRIVATE)

    override suspend fun saveReadingProgress(bookId: String, progress: Float) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putFloat("progress_$bookId", progress)
                .apply()
        }
    }

    override suspend fun getReadingProgress(bookId: String): Float {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getFloat("progress_$bookId", 0f)
        }
    }

    override fun getReadingSettings(): ReadingSettings {
        return ReadingSettings(
            fontSize = FontSize.valueOf(
                sharedPreferences.getString("font_size", FontSize.MEDIUM.name) ?: FontSize.MEDIUM.name
            ),
            lineSpacing = LineSpacing.valueOf(
                sharedPreferences.getString("line_spacing", LineSpacing.MEDIUM.name) ?: LineSpacing.MEDIUM.name
            ),
            theme = ReaderTheme.valueOf(
                sharedPreferences.getString("reader_theme", ReaderTheme.SYSTEM.name) ?: ReaderTheme.SYSTEM.name
            )
        )
    }

    override suspend fun saveReadingSettings(settings: ReadingSettings) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString("font_size", settings.fontSize.name)
                .putString("line_spacing", settings.lineSpacing.name)
                .putString("reader_theme", settings.theme.name)
                .apply()
        }
    }
}