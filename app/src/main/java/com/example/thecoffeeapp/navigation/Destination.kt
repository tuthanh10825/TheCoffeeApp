package com.example.thecoffeeapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument


interface Dest {
    val icon: ImageVector
    val route: String
}

object Home : Dest {
    override val icon = Icons.Default.Store
    override val route = "home"
}

object Reward : Dest {
    override val icon = Icons.Default.CardGiftcard
    override val route = "reward"
}

object Orders : Dest {
    override val icon = Icons.Default.Receipt
    override val route = "orders"
}

object Profile: Dest {
    override val icon = Icons.Default.Person
    override val route = "profile"
}

object Redeem: Dest {
    override val icon = Icons.Default.Cake
    override val route = "redeem"
}

object CoffeeDetail: Dest {
    override val icon = Icons.Default.Store
    const val coffeeTypeArg = "coffeeType"
    val arguments = listOf(
        navArgument(coffeeTypeArg){
            type = NavType.IntType
        }
    )
    override val route = "coffee_detail"
}

object Cart: Dest {
    override val icon = Icons.Default.ShoppingCart
    override val route = "cart"
}

object OrderSuccess: Dest {
    override val icon = Icons.Default.Receipt
    override val route = "order_success"
}

val tabScreens = listOf(
    Home,
    Reward,
    Orders
)
