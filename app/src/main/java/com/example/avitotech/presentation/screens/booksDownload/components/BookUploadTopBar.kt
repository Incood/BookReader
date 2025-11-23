package com.example.avitotech.presentation.screens.booksDownload.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.avitotech.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookUploadTopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.book_download)) },
        colors = TopAppBarDefaults.topAppBarColors()
    )
}