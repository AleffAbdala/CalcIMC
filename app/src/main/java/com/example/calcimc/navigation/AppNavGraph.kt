package com.example.calcimc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calcimc.datasource.db.HealthDatabase
import com.example.calcimc.datasource.db.HealthRepository
import com.example.calcimc.view.Home
import com.example.calcimc.view.HistoryScreen
import com.example.calcimc.view.HistoryDetailScreen
import com.example.calcimc.viewmodel.HealthViewModel
import com.example.calcimc.viewmodel.HealthViewModelFactory

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // ===== ROOM =====
    val database = HealthDatabase.getInstance(context)
    val repository = HealthRepository(database.healthDao())

    // ===== VIEWMODEL =====
    val viewModel: HealthViewModel = viewModel(
        factory = HealthViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        // ===== HOME =====
        composable(Routes.HOME) {
            Home(
                viewModel = viewModel,
                onGoToHistory = {
                    navController.navigate(Routes.HISTORY)
                }
            )
        }

        // ===== HISTORY =====
        composable(Routes.HISTORY) {
            HistoryScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onGoToDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        // ===== DETAIL =====
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getLong("id") ?: return@composable

            HistoryDetailScreen(
                recordId = id,
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
