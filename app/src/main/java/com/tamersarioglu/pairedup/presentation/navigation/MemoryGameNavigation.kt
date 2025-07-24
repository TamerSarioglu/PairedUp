package com.tamersarioglu.pairedup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tamersarioglu.pairedup.domain.model.GameDifficulty

@Composable
fun MemoryGameNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToGameSetup = {
                    navController.navigate(Screen.GameSetup)
                },
                onNavigateToScores = {
                    navController.navigate(Screen.Scores)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings)
                }
            )
        }

        composable<Screen.GameSetup> {
            GameSetupScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onStartGame = { playerName, difficulty ->
                    navController.navigate(
                        Screen.Game(
                            playerName = playerName,
                            difficulty = difficulty.name
                        )
                    )
                }
            )
        }

        composable<Screen.Game> {
            val args = it.toRoute<Screen.Game>()
            GameScreen(
                playerName = args.playerName,
                difficulty = GameDifficulty.valueOf(args.difficulty),
                onNavigateBack = {
                    navController.popBackStack(Screen.Home, inclusive = false)
                },
                onNavigateToScores = {
                    navController.navigate(Screen.Scores) {
                        popUpTo(Screen.Home)
                    }
                }
            )
        }

        composable<Screen.Scores> {
            ScoresScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}