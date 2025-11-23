package com.example.avitotech.presentation.screens.login.components

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
fun LoginTopBar(onNavigateToSignUp: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.log_in_to_account),
            )
        },
        actions = {
            TextButton(onClick = onNavigateToSignUp) {
                Text(
                    text = stringResource(R.string.registration),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}