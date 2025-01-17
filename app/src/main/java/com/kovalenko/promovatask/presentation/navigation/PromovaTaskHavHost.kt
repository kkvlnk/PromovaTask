package com.kovalenko.promovatask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun PromovaTaskNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        startDestination = Movies,
        navController = navController,
        modifier = modifier
    ) {
        moviesGraph()
        favoritesGraph()
    }
}