# 🌸 Personal Expense Management

A premium, modern Android application built with **Kotlin** and **Jetpack Compose** designed for effortless and stylish spending tracking.

![App Logo](docs/assets/logo.png)

## ✨ Premium Design Aesthetic
This app features a custom-crafted **Pink Vibe** design system, tailored for a high-end, elegant user experience:
- **Harmonious Palette**: A curated selection of Hot Pink, Misty Rose, and Lavender Blush tones.
- **Modern Typography**: Bold, readable fonts that emphasize important financial data.
- **Premium Shapes**: Custom 32dp rounded corners for all cards and buttons to provide a soft, contemporary feel.
- **Dynamic Gradients**: Rich vertical and horizontal gradients on key UI elements like the Total Spending dashboard and progress bars.

## 🚀 Key Features
- **Smart Dashboard**: Instant overview of total spending with transaction counts.
- **Effortless Logging**: Add expenses with titles, categories, and descriptions in a clean, intuitive form.
- **Financial History**: A beautiful list view of all transactions with quick-delete functionality.
- **Intelligent Budgeting**: Real-time monthly budget monitoring with visual warnings:
  - 💖 **Healthy**: Budget is well within limits.
  - ⚠️ **Near Limit**: Notifies you when you've reached 80% of your budget.
  - 🚨 **Exceeded**: Clear warnings if you spend beyond your set limit.
- **Category Analytics**: Detailed breakdown of spending by category with animated-style progress bars.

## 🛠️ Tech Stack & Architecture
This project follows the **Clean Architecture** principles and the **MVVM** (Model-View-ViewModel) pattern:
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Declarative UI)
- **Local Database**: Room (SQLite abstraction for persistent storage)
- **Navigation**: Jetpack Compose Navigation
- **State Management**: Kotlin Flow & StateFlow (Reactive UI updates)

## 📁 Project Structure
- `data/`: Room entities, DAO interfaces, and the Repository pattern.
- `ui/viewmodel/`: Core business logic, spending calculations, and budget state management.
- `ui/screens/`: Composable screens for Home, Add Expense, List, and Stats.
- `ui/theme/`: Custom Design System including Colors, Typography, and Shapes.

## 💻 Installation & Build
To build the project from the root directory:
```powershell
.\gradlew.bat installDebug
```

---
*Designed with 💖 for style and financial clarity.*
