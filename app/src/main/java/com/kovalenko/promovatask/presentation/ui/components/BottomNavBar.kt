package com.kovalenko.promovatask.presentation.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.kovalenko.promovatask.presentation.navigation.TopLevelRoute

@Composable
fun BottomNavBar(
    routes: List<TopLevelRoute<out Any>>,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    onClick: (TopLevelRoute<out Any>) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        routes.forEach { route ->
            NavigationBarItem(
                icon = { Icon(imageVector = route.icon, contentDescription = null) },
                label = { Text(stringResource(route.nameRes)) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true,
                onClick = { onClick(route) }
            )
        }
    }
}