package com.kovalenko.promovatask.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any>(@StringRes val nameRes: Int, val route: T, val icon: ImageVector)