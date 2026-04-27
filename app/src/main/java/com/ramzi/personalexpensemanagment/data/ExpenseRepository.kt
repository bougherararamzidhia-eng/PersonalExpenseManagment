package com.ramzi.personalexpensemanagment.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    fun getExpensesByCategory(category: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category)
    }

    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
    }

    fun getTotalSpending(): Flow<Double> {
        return expenseDao.getTotalSpending().map { it ?: 0.0 }
    }

    fun getCategoryTotal(category: String): Flow<Double> {
        return expenseDao.getCategoryTotal(category).map { it ?: 0.0 }
    }

    fun getAllCategories(): Flow<List<String>> {
        return expenseDao.getAllCategories()
    }
}

