package com.example.thecoffeeapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.example.thecoffeeapp.ui.component.createNotificationChannel
import com.example.thecoffeeapp.ui.component.sendNotification
import com.example.thecoffeeapp.viewmodel.CoffeeViewModel
import com.example.thecoffeeapp.viewmodel.CoffeeViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel(this)

        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            //                                        grantResults: IntArray)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001 // your request code
            )

        }

        val database = CoffeeRoomDatabase.getDatabase(applicationContext)
        val repository =
            Repository(database.profileDao(), database.orderDao(), database.rewardDao())

        val factory = CoffeeViewModelFactory(repository)
        val coffeeViewModel = ViewModelProvider(this, factory)[CoffeeViewModel::class.java]


        val navigateTo = intent?.getStringExtra("navigateTo")

        val coffeeType = intent?.getIntExtra("coffeeType", -1)
        val status = intent?.getStringExtra("status")

        setContent {
            var isSplashDone by rememberSaveable { mutableStateOf(false) }

            createNotificationChannel(this)

            LaunchedEffect(Unit) {
                if (coffeeType != null && navigateTo != null) {
                    isSplashDone = true
                } else {
                    delay(2000)
                    isSplashDone = true
                }
            }


            TheCoffeeAppTheme(
                darkTheme = false, // Change this to true for dark theme
                dynamicColor = false // Change this to true for dynamic color
            ) {
                if (!isSplashDone) {
                    SplashScreen()
                }
                else {
                    MyApp(
                        this,
                        coffeeViewModel,
                        startDest = navigateTo,
                        coffeeType = coffeeType,
                        status = status
                    )
                }
           }
        }
    }
}


// TODO: Extract the back navigation logic to a separate function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    context : Context,
    coffeeViewModel: CoffeeViewModel,
    startDest: String? = Home.route,
    coffeeType: Int? = null,
    status: String? = null
) {
    var navController = rememberNavController()
    var currentScreen by remember { mutableStateOf<Dest>(Home) }

    var isShowingBottomBar by remember { mutableStateOf(true) }
    val redeemList by remember {mutableStateOf(sampleRedeemList)}



    val profileInfo = coffeeViewModel.userInfo
    val profileInfoState by profileInfo.collectAsState()

    LaunchedEffect(profileInfoState) {
        if (profileInfoState == null) {
            coffeeViewModel.createTempProfile()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var hasNavigatedFromIntent by remember { mutableStateOf(false) }

    // val onGoingOrderListState = coffeeViewModel.onGoingOrderList.collectAsState()
    // val orderHistoryListState = coffeeViewModel.historyOrderList.collectAsState()
    // val showBottomSheet = remember { mutableStateOf(false) }

    // 🔥 Only navigate once after splash
    LaunchedEffect(startDest, coffeeType) {
        if (!hasNavigatedFromIntent && startDest == CoffeeDetail.route && coffeeType != null) {
            hasNavigatedFromIntent = true
            isShowingBottomBar = false
            navController.navigate("${CoffeeDetail.route}/${coffeeType}/false")
        }
        else if (!hasNavigatedFromIntent && startDest == Orders.route && status != null) {
            hasNavigatedFromIntent = true
            isShowingBottomBar = true
            navController.navigate("${Orders.route}?status=${status}")
        }
    }

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
            context = context,
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


