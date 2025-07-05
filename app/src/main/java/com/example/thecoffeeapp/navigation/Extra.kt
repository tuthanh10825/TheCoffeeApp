package com.example.thecoffeeapp.navigation

import androidx.navigation.NavHostController

fun handleBackNavigation(
    navController: NavHostController,
    setBottomBarVisible: (Boolean) -> Unit,
    setCurrentScreen: (Dest) -> Unit
) {
    val previousRoute = navController.previousBackStackEntry?.destination?.route
    setBottomBarVisible(previousRoute in listOf(Home.route, Reward.route, Orders.route))
    setCurrentScreen(
        when (previousRoute) {
            Home.route -> Home
            Reward.route -> Reward
            Orders.route -> Orders
            else -> Home
        }
    )
    navController.navigateUp()
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }