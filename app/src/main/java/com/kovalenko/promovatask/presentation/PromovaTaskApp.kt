package com.kovalenko.promovatask.presentation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kovalenko.promovatask.R
import com.kovalenko.promovatask.presentation.navigation.Favorites
import com.kovalenko.promovatask.presentation.navigation.Movies
import com.kovalenko.promovatask.presentation.navigation.PromovaTaskNavHost
import com.kovalenko.promovatask.presentation.navigation.TopLevelRoute
import com.kovalenko.promovatask.presentation.ui.components.BottomNavBar
import com.kovalenko.promovatask.presentation.ui.theme.PromovaTaskTheme

@Composable
fun PromovaTaskApp() {
    PromovaTaskTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Scaffold(
            bottomBar = {
                val routes = remember {
                    listOf(
                        TopLevelRoute(R.string.movies, Movies, Icons.AutoMirrored.Filled.List),
                        TopLevelRoute(R.string.favorites, Favorites, Icons.Filled.Favorite),
                    )
                }
                BottomNavBar(routes = routes, currentDestination = currentDestination) {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        ) { innerPadding ->
            PromovaTaskNavHost(
                navController,
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )
        }
    }
}