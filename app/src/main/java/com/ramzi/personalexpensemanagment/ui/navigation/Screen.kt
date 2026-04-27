package com.ramzi.personalexpensemanagment.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddExpense : Screen("add_expense")
    object ExpenseList : Screen("expense_list")
    object Stats : Screen("stats")
}

