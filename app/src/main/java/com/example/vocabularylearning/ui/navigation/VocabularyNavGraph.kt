package com.example.vocabularylearning.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vocabularylearning.ui.home.HomeDestination
import com.example.vocabularylearning.ui.home.HomeScreen
import com.example.vocabularylearning.ui.settings.DisplaySettingsDestination
import com.example.vocabularylearning.ui.settings.DisplaySettingsScreen
import com.example.vocabularylearning.ui.word.WordEntryScreen
import com.example.vocabularylearning.ui.word.WordEntryDestination
import com.example.vocabularylearning.ui.word.WordEditScreen
import com.example.vocabularylearning.ui.word.WordEditDestination

@Composable
fun VocabularyNavHost(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    )
    {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToWordEntry = { navController.navigate(WordEntryDestination.route) },
                navigateToWordEdit = { navController.navigate("${WordEditDestination.route}/$it") },
                navigateToDisplaySettings = { navController.navigate(DisplaySettingsDestination.route) },
                windowSize = windowSize
            )
        }
        composable(route = DisplaySettingsDestination.route) {
            DisplaySettingsScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(route = WordEntryDestination.route) {
            WordEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = WordEditDestination.routeWithArgs,
            arguments = listOf(navArgument(WordEditDestination.wordIdArg) {
                type = NavType.IntType
            })
        ) {
            WordEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}