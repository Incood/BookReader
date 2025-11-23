package com.example.avitotech.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.avitotech.R
import com.example.avitotech.presentation.screens.profile.components.AppTopAppBar
import com.example.avitotech.presentation.screens.profile.components.ProfileContent
import com.example.avitotech.presentation.screens.profile.state.ProfileState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    onNavigateToAuth: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState = viewModel.profileState.collectAsState().value
    val signOutResult = viewModel.signOutResult.collectAsState().value

    println("DEBUG: ProfileScreen state = $profileState")

    LaunchedEffect(key1 = viewModel.signOutResult) {
        viewModel.signOutResult.collectLatest { result ->
            if (result?.isSuccess == true) {
                onNavigateToAuth()
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = stringResource(R.string.profile),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = profileState) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Success -> {
                    ProfileContent(
                        user = state.user,
                        onPhotoClick = {},
                        onSignOut = { viewModel.signOut() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ProfileState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadUserProfile() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}