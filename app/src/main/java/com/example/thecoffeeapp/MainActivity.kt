package com.example.thecoffeeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import androidx.compose.material3.FabPosition
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.thecoffeeapp.data.local.db.sampleRedeemList
import kotlinx.coroutines.delay
import com.example.thecoffeeapp.data.local.db.CoffeeRoomDatabase
import com.example.thecoffeeapp.data.repository.Repository
import com.example.thecoffeeapp.ui.component.BottomNavBar
import com.example.thecoffeeapp.ui.screens.*
import com.example.thecoffeeapp.navigation.*
import com.example.thecoffeeapp.viewmodel.CoffeeViewModel
import com.example.thecoffeeapp.viewmodel.CoffeeViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = CoffeeRoomDatabase.getDatabase(applicationContext)
        val repository =
            Repository(database.profileDao(), database.orderDao(), database.rewardDao())

        val factory = CoffeeViewModelFactory(repository)
        val coffeeViewModel = ViewModelProvider(this, factory)[CoffeeViewModel::class.java]
        setContent {
            var isSplashDone by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(2000)
                isSplashDone = true
            }

            TheCoffeeAppTheme {
                if (isSplashDone) {
                    MyApp(coffeeViewModel)
                } else {
                    SplashScreen()
                }

            }
        }
    }
}


// TODO: Extract the back navigation logic to a separate function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    coffeeViewModel: CoffeeViewModel
) {
    var navController = rememberNavController()
    var currentScreen by remember { mutableStateOf<Dest>(Home) }


    var isShowingBottomBar by remember { mutableStateOf(true) }
    val redeemList by remember {mutableStateOf(sampleRedeemList)}

    if (coffeeViewModel.userInfo.collectAsState().value == null) {
        coffeeViewModel.createTempProfile()
    }

    val profileInfo = coffeeViewModel.userInfo
    val profileInfoState by profileInfo.collectAsState()

    val onGoingOrderListState = coffeeViewModel.onGoingOrderList.collectAsState()
    val orderHistoryListState = coffeeViewModel.historyOrderList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
        AppNavHost(
            navController = navController,
            padding = padding,
            coffeeViewModel = coffeeViewModel,
            profileInfoState = profileInfoState,
            onSetBottomBarVisible = { isShowingBottomBar = it },
            onSetCurrentScreen = { currentScreen = it },
            snackbarHostState = snackbarHostState,
            scope = scope,
            redeemList = redeemList
        )
    }
}


