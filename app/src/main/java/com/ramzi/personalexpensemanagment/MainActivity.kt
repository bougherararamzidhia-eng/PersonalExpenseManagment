package com.ramzi.personalexpensemanagment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.ramzi.personalexpensemanagment.data.ExpenseDatabase
import com.ramzi.personalexpensemanagment.data.ExpenseRepository
import com.ramzi.personalexpensemanagment.ui.navigation.AppNavigation
import com.ramzi.personalexpensemanagment.ui.theme.PersonalExpenseManagmentTheme
import com.ramzi.personalexpensemanagment.ui.viewmodel.ExpenseViewModel
import com.ramzi.personalexpensemanagment.ui.viewmodel.ExpenseViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Database and Repository
        val database = ExpenseDatabase.getDatabase(this)
        val expenseDao = database.expenseDao()
        val repository = ExpenseRepository(expenseDao)
        val viewModelFactory = ExpenseViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)

        setContent {
            PersonalExpenseManagmentTheme {
                val navController = rememberNavController()
                AppNavigation(navController, viewModel)
            }
        }
    }
}
