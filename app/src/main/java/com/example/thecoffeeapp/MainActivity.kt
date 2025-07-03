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
import androidx.compose.ui.unit.dp
import com.example.thecoffeeapp.ui.theme.TheCoffeeAppTheme
import androidx.compose.material3.FabPosition
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thecoffeeapp.data.sampleProfileInfo
import com.example.thecoffeeapp.data.sampleRedeemList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.thecoffeeapp.data.CoffeeRoomDatabase
import com.example.thecoffeeapp.data.Repository
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = CoffeeRoomDatabase.getDatabase(applicationContext)
        val repository = Repository(database.profileDao(), database.orderDao(), database.rewardDao())

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
@Composable
fun MyApp(
    coffeeViewModel: CoffeeViewModel
) {
    var navController = rememberNavController()
    var currentScreen by remember { mutableStateOf<Dest>(Home) }
    var isShowingBottomBar by rememberSaveable { mutableStateOf(true) }


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
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable(Home.route) {
                HomeScreen(
                coffeeCnt = profileInfoState?.coffeeCnt ?: 0,
                username = profileInfoState?.name ?: "",
                    onProfileClick = {
                        isShowingBottomBar = false
                        navController.navigateSingleTopTo(Profile.route)
                    },
                    onCartClick = {
                        isShowingBottomBar = false
                        navController.navigateSingleTopTo(Cart.route)
                    },
                    onItemClick = { coffeeType ->
                        isShowingBottomBar = false
                        navController.navigateSingleTopTo(CoffeeDetail.route + "/${coffeeType.text}/false")
                    },
                    modifier = Modifier.padding(padding),
                    onRedeemClick = {
                        coffeeViewModel.updateLoyaltyCoffeeCount(
                            coffeeViewModel.coffeeCnt.value - 8
                        )
                    },
                    coffeeTypeList = coffeeViewModel.coffeeTypeList
                )
            }
            composable(Reward.route) {
                RewardScreen(
                    onRedeemReward = {
                        isShowingBottomBar = false
                        navController.navigateSingleTopTo(Redeem.route)
                    },
                    modifier = Modifier.padding(padding),
                    coffeeViewModel,
                    onRedeemLoyalty = {
                        coffeeViewModel.updateLoyaltyCoffeeCount(
                            coffeeViewModel.coffeeCnt.value - 8
                        )
                    }
                )
            }
            composable(Orders.route) {
                OrderScreen(
                    onGivenOrder = { orderInfo ->
                        coffeeViewModel.moveToCompletedOrders(orderInfo)
                    },
                    onGoingOrderList = onGoingOrderListState.value,
                    orderHistoryList = orderHistoryListState.value,
                    modifier = Modifier.padding(padding)
                )
            }
            composable(Profile.route) {
                ProfileScreen(modifier = Modifier.padding(padding),
                    onBackButton = {
                        isShowingBottomBar = true
                        navController.navigateUp()
                    },
                    coffeeViewModel
                )
            }
            composable(Redeem.route) {
                RedeemScreen(
                    redeemList = redeemList,
                    onBackButton = {
                        isShowingBottomBar = true
                        navController.navigateUp()
                    },
                    onRedeem = { coffeeType, redeemInfo ->
                        val point = coffeeViewModel.redeemPoint
                        if (point.value >= redeemInfo.pointsRequired) {
                            coffeeViewModel.updateRedeemPoint(
                                point.value - redeemInfo.pointsRequired
                            )
                            isShowingBottomBar = false
                            navController.navigateSingleTopTo("${CoffeeDetail.route}/${coffeeType.text}/true")
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Not enough points to redeem this item.",
                                    actionLabel = "OK",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            composable(
                route = "${CoffeeDetail.route}/{${CoffeeDetail.coffeeTypeArg}}/{isRedeemed}",
                arguments = CoffeeDetail.arguments + listOf(
                    navArgument("isRedeemed") {
                        type = NavType.BoolType
                        defaultValue = false // optional
                    }
                    
                ),
            ) {
                val coffeeTextId = it.arguments?.getInt(CoffeeDetail.coffeeTypeArg)
                val coffeeData = coffeeViewModel.getCoffeeDataById(coffeeTextId?:0)
                val coffeeDetail = rememberCoffeeDetailData(CoffeeDetailDataState())


                // TODO: Hand the redeem case.
                val isRedeem = it.arguments?.getBoolean("isRedeemed") ?: false

                CoffeeDetailScreen(
                    isRedeemed = isRedeem,
                    coffeeData = coffeeData,
                    coffeeDetailDataState = coffeeDetail,
                    onBackButton = {
                        val previousDestination = navController.previousBackStackEntry?.destination?.route
                        isShowingBottomBar = previousDestination in listOf(Home.route, Reward.route, Orders.route)
                        currentScreen = when (previousDestination) {
                            Home.route -> Home
                            Reward.route -> Reward
                            Orders.route -> Orders
                            else -> Home // Default to Home if no previous destination matches
                        }
                        navController.navigateUp()
                    },
                    onAddToCart = {
                        isShowingBottomBar = false
                        coffeeViewModel.addCoffeeOrderItem(
                            isRedeemed = isRedeem,
                            coffeeData?: CoffeeTypeData(
                            drawable = R.drawable.ic_coffee,
                            text = 0, // Fallback in case of null
                        ),
                            coffeeDetail
                        )
                        navController.navigate(Cart.route) {
                            popUpTo(CoffeeDetail.route) { inclusive = true } // This removes CoffeeDetail from backstack
                            launchSingleTop = true
                        }
                    },
                    onRightButtonClick = {
                        isShowingBottomBar = false
                        navController.navigateSingleTopTo(Cart.route)
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            composable(Cart.route) {
                val cartItems = coffeeViewModel.coffeeBuyList
                CartScreen(
                    cartItems = cartItems,
                    onBackButton = {
                        val previousDestination = navController.previousBackStackEntry?.destination?.route
                        isShowingBottomBar = previousDestination == Home.route
                        navController.navigateUp()
                    },
                    onCheckoutClick = {
                        isShowingBottomBar = false
                        coffeeViewModel.buy()
                        navController.navigateSingleTopTo(OrderSuccess.route)
                    },
                    modifier = Modifier.padding(padding),
                    onDeleteItem = {buyItem ->
                        coffeeViewModel.removeCoffeeOrderItem(buyItem) // Remove item from cart
                    }
                )
            }
            composable(OrderSuccess.route) {
                OrderSuccessScreen(
                    onTrackOrderClick = {
                        isShowingBottomBar = true
                        currentScreen = Orders
                        navController.navigateSingleTopTo(Orders.route)
                    },
                )
            }
        }
    }
}


fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }

