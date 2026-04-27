package com.ramzi.personalexpensemanagment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ramzi.personalexpensemanagment.data.Expense
import com.ramzi.personalexpensemanagment.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    companion object {
        const val ALL_CATEGORIES = "All"
    }

    private val allStoredExpenses = repository
        .getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val selectedCategoryFlow = kotlinx.coroutines.flow.MutableStateFlow(ALL_CATEGORIES)
    val selectedCategory: StateFlow<String> = selectedCategoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ALL_CATEGORIES)

    private val monthlyBudgetLimitFlow = kotlinx.coroutines.flow.MutableStateFlow(10_000.0)
    val monthlyBudgetLimit: StateFlow<Double> = monthlyBudgetLimitFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 10_000.0)

    val allExpenses: StateFlow<List<Expense>> = combine(
        allStoredExpenses,
        selectedCategoryFlow
    ) { expenses, selected ->
        if (selected == ALL_CATEGORIES) {
            expenses
        } else {
            expenses.filter { it.category == selected }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalSpending: StateFlow<Double> = allStoredExpenses
        .map { expenses -> expenses.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val categories: StateFlow<List<String>> = allStoredExpenses
        .map { expenses ->
            val existing = expenses.map { it.category }.distinct().sorted()
            listOf(ALL_CATEGORIES) + existing
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), listOf(ALL_CATEGORIES))

    val categoryTotals: StateFlow<Map<String, Double>> = allStoredExpenses
        .map { expenses ->
            expenses
                .groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    val currentMonthSpending: StateFlow<Double> = allStoredExpenses
        .map { expenses ->
            val zoneId = ZoneId.systemDefault()
            val now = LocalDate.now(zoneId)
            expenses
                .filter { expense ->
                    val expenseDate = Instant.ofEpochMilli(expense.date).atZone(zoneId).toLocalDate()
                    expenseDate.month == now.month && expenseDate.year == now.year
                }
                .sumOf { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val budgetUsagePercent: StateFlow<Double> = combine(
        currentMonthSpending,
        monthlyBudgetLimitFlow
    ) { monthlySpent, budgetLimit ->
        if (budgetLimit <= 0.0) 0.0 else (monthlySpent / budgetLimit) * 100.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val isBudgetExceeded: StateFlow<Boolean> = combine(
        currentMonthSpending,
        monthlyBudgetLimitFlow
    ) { monthlySpent, budgetLimit ->
        budgetLimit > 0.0 && monthlySpent > budgetLimit
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val isBudgetNearLimit: StateFlow<Boolean> = budgetUsagePercent
        .map { usage -> usage in 80.0..100.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun addExpense(title: String, amount: Double, category: String, date: Long, description: String) {
        viewModelScope.launch {
            val expense = Expense(
                title = title,
                amount = amount,
                category = category,
                date = date,
                description = description
            )
            repository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun setCategoryFilter(category: String) {
        selectedCategoryFlow.value = category
    }

    fun setMonthlyBudgetLimit(limit: Double) {
        if (limit >= 0.0) {
            monthlyBudgetLimitFlow.value = limit
        }
    }
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

