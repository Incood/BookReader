package com.example.avitotech.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.avitotech.R

@Composable
fun MainApp() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val mainScreens = listOf(Routes.BOOKS, Routes.UPLOAD, Routes.PROFILE)
            val isMainScreen = currentRoute in mainScreens

            if (isMainScreen) {
                AppBottomNavigation(navController = navController)
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_library),
                    contentDescription = stringResource(R.string.my_books),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(R.string.my_books)) },
            selected = currentDestination == Routes.BOOKS,
            onClick = {
                navController.navigate(Routes.BOOKS) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = stringResource(R.string.download_book),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(R.string.download_book)) },
            selected = currentDestination == Routes.UPLOAD,
            onClick = {
                navController.navigate(Routes.UPLOAD) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.profile)
                )
            },
            label = { Text(stringResource(R.string.profile)) },
            selected = currentDestination == Routes.PROFILE,
            onClick = {
                navController.navigate(Routes.PROFILE) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

fun NavController.findStartDestination(): NavDestination {
    val startDestination = graph.startDestinationId
    return graph.findNode(startDestination)!!
}