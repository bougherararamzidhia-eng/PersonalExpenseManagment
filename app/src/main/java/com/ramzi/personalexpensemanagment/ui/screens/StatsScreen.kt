package com.ramzi.personalexpensemanagment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramzi.personalexpensemanagment.ui.viewmodel.ExpenseViewModel

val categoryColors = mapOf(
    "Food" to Color(0xFFFF6B6B),
    "Transport" to Color(0xFF4ECDC4),
    "Entertainment" to Color(0xFFFFE66D),
    "Education" to Color(0xFF95E1D3),
    "Health" to Color(0xFFC7CEEA),
    "Other" to Color(0xFFB0BEC5)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: ExpenseViewModel,
    onNavigateBack: () -> Unit
) {
    val totalSpending by viewModel.totalSpending.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val displayCategories = categories.filter { it != ExpenseViewModel.ALL_CATEGORIES }
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val monthlyBudgetLimit by viewModel.monthlyBudgetLimit.collectAsState()
    val currentMonthSpending by viewModel.currentMonthSpending.collectAsState()
    val budgetUsagePercent by viewModel.budgetUsagePercent.collectAsState()
    val isBudgetNearLimit by viewModel.isBudgetNearLimit.collectAsState()
    val isBudgetExceeded by viewModel.isBudgetExceeded.collectAsState()
    var budgetInput by remember(monthlyBudgetLimit) { mutableStateOf(monthlyBudgetLimit.toInt().toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics & Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Spending Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Spending",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "%.2f DA".format(totalSpending),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Category Breakdown Title
            Text(
                text = "Spending by Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Category Cards
            if (displayCategories.isEmpty()) {
                Text(
                    text = "No spending data yet",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp
                )
            } else {
                displayCategories.forEach { category ->
                    val amount = categoryTotals[category] ?: 0.0
                    val percentage = if (totalSpending > 0) {
                        (amount / totalSpending * 100)
                    } else {
                        0.0
                    }

                    CategoryCard(
                        category = category,
                        amount = amount,
                        percentage = percentage,
                        color = categoryColors[category] ?: Color.Gray
                    )
                }
            }

            // Budget Warning Section
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "💎 Monthly Budget",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = budgetInput,
                        onValueChange = { input ->
                            budgetInput = input
                            val parsed = input.toDoubleOrNull()
                            if (parsed != null) {
                                viewModel.setMonthlyBudgetLimit(parsed)
                            }
                        },
                        label = { Text("Budget limit (DA)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Spent this month:",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "%.2f DA".format(currentMonthSpending),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Budget usage:",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "%.1f%%".format(budgetUsagePercent),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isBudgetExceeded) Color.Red else MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val warningText = when {
                        isBudgetExceeded -> "⚠️ Oops! You've gone over your budget."
                        isBudgetNearLimit -> "⚠️ Careful! You're almost at your limit."
                        else -> "✨ Great job! Your budget is safe."
                    }

                    androidx.compose.material3.Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = warningText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp),
                            color = if (isBudgetExceeded) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: String,
    amount: Double,
    percentage: Double,
    color: Color
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Text(
                        text = category,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 12.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "%.2f DA".format(amount),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Premium Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (percentage / 100).toFloat().coerceIn(0f, 1f))
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(color.copy(alpha = 0.7f), color)
                            )
                        )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "%.1f%%".format(percentage),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = color.copy(alpha = 0.8f)
                )
            }
        }
    }
}

