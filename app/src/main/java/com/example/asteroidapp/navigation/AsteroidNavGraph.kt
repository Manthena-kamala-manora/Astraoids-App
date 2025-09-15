
package com.example.asteroidapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.asteroidapp.api.models.AsteroidModel
import com.example.asteroidapp.features.detail.view.AsteroidDetailDestination
import com.example.asteroidapp.features.detail.view.AsteroidDetailScreen
import com.example.asteroidapp.features.home.HomeDestination
import com.example.asteroidapp.features.home.HomeScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AsteroidNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route, modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(navigateToItemDetail = { asteroid ->

                val asteroidJson = Json.encodeToString<AsteroidModel>(asteroid)
                Timber.d("Navigating to AsteroidDetailScreen with asteroid: $asteroidJson")
                navController.navigate("${AsteroidDetailDestination.route}/${asteroidJson}")

            })
        }
        composable(
            route = AsteroidDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(AsteroidDetailDestination.ASTEROID_MODEL_ARG) {
                type = NavType.StringType
            })
        ) {
            AsteroidDetailScreen(navigateBack = { navController.popBackStack() })
        }
    }
}
