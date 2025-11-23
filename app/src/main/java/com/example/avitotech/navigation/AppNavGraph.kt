package com.example.avitotech.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.avitotech.presentation.screens.booksDownload.BookUploadScreen
import com.example.avitotech.presentation.screens.booksMain.BooksScreen
import com.example.avitotech.presentation.screens.login.LoginScreen
import com.example.avitotech.presentation.screens.profile.ProfileScreen
import com.example.avitotech.presentation.screens.reader.BookReaderScreen
import com.example.avitotech.presentation.screens.registration.RegistrationScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.BOOKS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.REGISTRATION)
                }
            )
        }

        composable(Routes.REGISTRATION) {
            RegistrationScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.BOOKS) {
                        popUpTo(Routes.REGISTRATION) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTRATION) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.BOOKS) {
            BooksScreen(
                onNavigateToReader = { bookId ->
                    navController.navigate("reader/$bookId")
                }
            )
        }

        composable(Routes.UPLOAD) {
            BookUploadScreen()
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateToAuth = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.PROFILE) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "reader/{bookId}",
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookReaderScreen(
                bookId = bookId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}