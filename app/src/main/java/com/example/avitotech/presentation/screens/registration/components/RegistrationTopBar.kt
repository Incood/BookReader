package com.example.avitotech.presentation.screens.registration.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.avitotech.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationTopBar(onNavigateToLogin: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}