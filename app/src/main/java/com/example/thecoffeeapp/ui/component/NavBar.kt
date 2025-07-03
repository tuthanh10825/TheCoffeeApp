package com.example.thecoffeeapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import com.example.thecoffeeapp.navigation.*

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    currentScreen: Dest = Home,
    onItemSelected: (Dest) -> Unit = { /* No-op */ } // Callback for item selection
) {
    NavigationBar (
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(24.dp)),
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
            tabScreens.forEach { dest ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.route,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = (dest == currentScreen),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,     // selected icon
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // unselected icon
                    indicatorColor = Color.Transparent
                ),
                onClick = { onItemSelected(dest) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    TheCoffeeAppTheme {
        NavigationBar {
            BottomNavBar()
        }
    }
}