package com.tamersarioglu.pairedup

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tamersarioglu.pairedup.domain.model.GameSettings
import com.tamersarioglu.pairedup.domain.usecase.GetSettingsUseCase
import com.tamersarioglu.pairedup.presentation.navigation.MemoryGameNavigation
import com.tamersarioglu.pairedup.presentation.ui.theme.PairedUpTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getSettingsUseCase: GetSettingsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val settings by getSettingsUseCase.getGameSettings().collectAsState(
                initial = GameSettings()
            )

            PairedUpTheme(darkTheme = settings.isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

                    MemoryGameNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}