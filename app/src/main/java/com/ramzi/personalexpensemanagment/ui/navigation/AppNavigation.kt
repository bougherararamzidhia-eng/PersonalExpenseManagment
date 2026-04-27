package com.ramzi.personalexpensemanagment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramzi.personalexpensemanagment.ui.screens.AddExpenseScreen
import com.ramzi.personalexpensemanagment.ui.screens.ExpenseListScreen
import com.ramzi.personalexpensemanagment.ui.screens.HomeScreen
import com.ramzi.personalexpensemanagment.ui.screens.StatsScreen
import com.ramzi.personalexpensemanagment.ui.viewmodel.ExpenseViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: ExpenseViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddExpenseClick = { navController.navigate(Screen.AddExpense.route) },
                onViewExpensesClick = { navController.navigate(Screen.ExpenseList.route) },
                onViewStatsClick = { navController.navigate(Screen.Stats.route) }
            )
        }

        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Stats.route) {
            StatsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

