package com.example.avitotech.presentation.screens.booksDownload.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avitotech.presentation.screens.booksDownload.state.BookUploadEvent
import com.example.avitotech.presentation.screens.booksDownload.state.BookUploadState

@Composable
fun BookUploadContent(
    state: BookUploadState,
    onEvent: (BookUploadEvent) -> Unit,
    padding: PaddingValues,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onEvent(BookUploadEvent.FileSelected(
            it,
            fileName = "unknow"
        )) }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FileSelectionSection(
            fileName = state.fileName,
            onSelectFile = { launcher.launch("*/*") }
        )

        AnimatedVisibility(visible = state.selectedFileUri != null) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { onEvent(BookUploadEvent.TitleChanged(it)) },
                    label = { Text("Название книги") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.author,
                    onValueChange = { onEvent(BookUploadEvent.AuthorChanged(it)) },
                    label = { Text("Автор") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (state.isLoading) {
            UploadProgressSection(progress = state.uploadProgress)
        }

        state.error?.let { error ->
            ErrorMessage(error = error) {
                onEvent(BookUploadEvent.ResetState)
            }
        }

        if (state.isSuccess) {
            SuccessMessage {
                onEvent(BookUploadEvent.ResetState)
            }
        }

        AnimatedVisibility(
            visible = state.selectedFileUri != null &&
                !state.isLoading &&
                !state.isSuccess
        ) {
            Button(
                onClick = { onEvent(BookUploadEvent.UploadBook) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.title.isNotBlank() && state.author.isNotBlank()
            ) {
                Text("Загрузить книгу")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}