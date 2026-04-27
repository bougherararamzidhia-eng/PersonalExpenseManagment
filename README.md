# Personal Expense Management

A Kotlin + Jetpack Compose Android app to record, organize, and analyze personal spending.

## Features

- Add expenses with title, amount, category, date, and optional description
- View and delete expenses in a list
- Filter expenses by category (`All`, `Food`, `Transport`, etc.)
- Dashboard with total spending and transaction count
- Statistics screen with category breakdown and percentages
- Monthly budget control with warnings:
  - Near limit (>= 80%)
  - Exceeded limit (> 100%)

## Project Structure

- `app/src/main/java/com/ramzi/personalexpensemanagment/data`
  - `Expense.kt` (Room entity)
  - `ExpenseDao.kt` (DAO queries)
  - `ExpenseDatabase.kt` (Room database)
  - `ExpenseRepository.kt` (data abstraction)
- `app/src/main/java/com/ramzi/personalexpensemanagment/ui/viewmodel`
  - `ExpenseViewModel.kt` (state, filtering, totals, budget logic)
- `app/src/main/java/com/ramzi/personalexpensemanagment/ui/screens`
  - `HomeScreen.kt`
  - `AddExpenseScreen.kt`
  - `ExpenseListScreen.kt`
  - `StatsScreen.kt`
- `app/src/main/java/com/ramzi/personalexpensemanagment/ui/navigation`
  - `AppNavigation.kt`
  - `Screen.kt`

## Build and Test

Use PowerShell from the project root:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

## Notes

- Data is stored locally using Room.
- Default monthly budget is `10,000 DA` and can be changed from the Statistics screen.
- Room version is managed via `gradle/libs.versions.toml`.
