package com.kovalenko.promovatask.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Movies

fun NavGraphBuilder.moviesGraph() {
    composable<Movies> {  }
}