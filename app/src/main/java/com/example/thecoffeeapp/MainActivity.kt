package com.example.thecoffeeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thecoffeeapp.data.sampleProfileInfo
import com.example.thecoffeeapp.data.sampleRedeemList

class MainActivity : ComponentActivity() {
    val coffeeViewModel: CoffeeViewModel by viewModels<CoffeeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}


@Composable
fun MyApp() {
    TheCoffeeAppTheme {
        var navController = rememberNavController()
        var currentScreen by remember { mutableStateOf<Dest>(Home) }
        var isShowingBottomBar by rememberSaveable { mutableStateOf(true) }
        val profileInfo by remember {
            mutableStateOf<ProfileInfo>(sampleProfileInfo)
        }
        val redeemList by remember {mutableStateOf(sampleRedeemList)}
        Scaffold(
            floatingActionButton = {
                if (isShowingBottomBar) {
                    BottomNavBar(
                        Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        currentScreen = currentScreen,
                        onItemSelected = { dest ->
                            currentScreen = dest
                            navController.navigate(dest.route)
                        }
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            modifier = Modifier.systemBarsPadding().navigationBarsPadding()
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") {
                    HomeScreen(
                        username = profileInfo.name,
                        onProfileClick = {
                            isShowingBottomBar = false
                            navController.navigateSingleTopTo(Profile.route)
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
                composable("reward") {
                    RewardScreen(
                        onRedeemReward = {
                            isShowingBottomBar = false
                            navController.navigateSingleTopTo(Redeem.route)
                        },
                        modifier = Modifier.padding(padding))
                }
                composable("orders") {
                    OrderScreen(modifier = Modifier.padding(padding))
                }
                composable("profile") {
                    ProfileScreen(profileInfo, modifier = Modifier.padding(padding),
                        onBackButton = {
                            isShowingBottomBar = true
                            navController.navigateUp()
                        },
                        onTrailingIconClick = {}
                    )
                }
                composable("redeem") {
                    RedeemScreen(
                        redeemList = redeemList,
                        onBackButton = {
                            isShowingBottomBar = true
                            navController.navigateUp()
                        },
                        onRedeem = {
                            isShowingBottomBar = true
                            navController.navigateUp()
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }

