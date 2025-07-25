# 🧠 PairedUp - Memory Game

A modern Android memory card matching game built with Jetpack Compose, featuring clean architecture and smooth animations.

## 📱 About

PairedUp is an engaging memory game where players flip cards to find matching pairs. Test your memory skills across different difficulty levels while enjoying a polished user experience with customizable settings, score tracking, and haptic feedback.

## ✨ Features

### 🎮 Game Features
- **Two Difficulty Levels**: Easy (16 cards) and Hard (24 cards)
- **Timer Challenge**: Optional 60-second time limit
- **Score System**: Points based on performance and difficulty
- **Progress Tracking**: Visual feedback for matched pairs and attempts
- **Smooth Animations**: Card flip animations and transitions

### ⚙️ Customization
- **Dark/Light Theme**: Toggle between themes
- **Sound Effects**: Enable/disable game sounds
- **Haptic Feedback**: Vibration on card interactions
- **Timer Control**: Enable/disable time limits
- **Settings Persistence**: All preferences saved locally

### 📊 Data Management
- **Score History**: Track your best performances
- **Statistics**: View game statistics by difficulty
- **Data Reset**: Clear scores or reset settings to defaults
- **Local Storage**: All data stored securely on device

## 🏗️ Architecture

Built following **Clean Architecture** principles with **MVVM** pattern:

```
📁 presentation/     # UI Layer (Compose, ViewModels)
📁 domain/          # Business Logic (Use Cases, Models)
📁 data/            # Data Layer (Repository, DataStore, Room)
📁 di/              # Dependency Injection (Hilt)
```

### Key Components
- **Jetpack Compose** - Modern declarative UI
- **Hilt** - Dependency injection
- **Room** - Local database for scores
- **DataStore** - Settings persistence
- **Coroutines** - Asynchronous operations
- **Navigation Compose** - Screen navigation

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt |
| **Database** | Room |
| **Preferences** | DataStore |
| **Async** | Coroutines + Flow |
| **Navigation** | Navigation Compose |
| **Build** | Gradle (Kotlin DSL) |

## 📋 Requirements

- **Android API 24+** (Android 7.0)
- **Target SDK 36**
- **Kotlin 2.2.0**
- **Gradle 8.13+**

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 36

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/PairedUp.git
   cd PairedUp
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync and Build**
   - Let Gradle sync the project
   - Build the project (Build → Make Project)

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click Run or press Shift+F10

## 🎯 How to Play

1. **Setup**: Enter your name and choose difficulty level
2. **Game**: Tap cards to flip them and find matching pairs
3. **Match**: Remember card positions to make successful matches
4. **Win**: Match all pairs before time runs out (if timer enabled)
5. **Score**: Earn points based on performance and difficulty

### Scoring System
- **Base Points**: Awarded for each successful match
- **Time Bonus**: Extra points for quick completion
- **Difficulty Multiplier**: Higher scores for harder levels
- **Attempt Penalty**: Fewer points for more attempts

## 📁 Project Structure

```
app/src/main/java/com/tamersarioglu/pairedup/
├── data/
│   ├── local/
│   │   ├── database/        # Room database
│   │   └── datastore/       # Settings storage
│   ├── mapper/              # Data mappers
│   ├── provider/            # Resource providers
│   └── repository/          # Repository implementations
├── domain/
│   ├── model/               # Domain models
│   ├── repository/          # Repository interfaces
│   └── usecase/             # Business logic
├── presentation/
│   ├── components/          # Reusable UI components
│   ├── navigation/          # Navigation setup
│   ├── screens/             # Screen implementations
│   └── ui/theme/            # Theme and styling
├── di/                      # Dependency injection
└── utils/                   # Utility classes
```

## 🧪 Testing

The project includes unit tests for core business logic:

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 🔧 Configuration

### Build Variants
- **Debug**: Development build with debugging enabled
- **Release**: Production build with optimizations

### Permissions
- `VIBRATE`: For haptic feedback during gameplay

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License

## 👨‍💻 Author

**Tamer Sarioğlu**
- GitHub: [@tamersarioglu](https://github.com/tamersarioglu)

## 🙏 Acknowledgments

- Built as an Android Bootcamp graduation project
- Inspired by classic memory card games
- Uses Material Design 3 principles

---

*Enjoy testing your memory with PairedUp! 🧠✨*