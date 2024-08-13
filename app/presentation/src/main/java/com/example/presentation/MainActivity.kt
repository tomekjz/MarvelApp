package com.example.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.presentation.ui.theme.theme
import kotlinx.serialization.Serializable
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

open class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        setContent {
            theme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Character
                ) {
                    composable<Character> {
                        CharacterScreen(navController = navController)
                    }
                    composable<CharacterDetail> {
                        val args = it.toRoute<CharacterDetail>()
                        CharacterDetailScreen(args)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}

@Serializable
object Character

@Serializable
data class CharacterDetail(
    val name: String,
    val description: String,
    val image: String
)