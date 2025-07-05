package com.example.thecoffeeapp.navigation

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.thecoffeeapp.MainActivity
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.data.local.entity.ProfileInfo
import com.example.thecoffeeapp.data.local.entity.RedeemInfo
import com.example.thecoffeeapp.ui.component.createNotificationChannel
import com.example.thecoffeeapp.ui.component.sendNotification
import com.example.thecoffeeapp.ui.screens.CartScreen
import com.example.thecoffeeapp.ui.screens.CoffeeDetailDataState
import com.example.thecoffeeapp.ui.screens.CoffeeDetailScreen
import com.example.thecoffeeapp.ui.screens.CoffeeTypeData
import com.example.thecoffeeapp.ui.screens.HomeScreen
import com.example.thecoffeeapp.ui.screens.OrderScreen
import com.example.thecoffeeapp.ui.screens.OrderSuccessScreen
import com.example.thecoffeeapp.ui.screens.ProfileScreen
import com.example.thecoffeeapp.ui.screens.RedeemScreen
import com.example.thecoffeeapp.ui.screens.RewardScreen
import com.example.thecoffeeapp.ui.screens.rememberCoffeeDetailData
import com.example.thecoffeeapp.viewmodel.CoffeeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    context: Context,
    navController: NavHostController,
    padding: PaddingValues,
    coffeeViewModel: CoffeeViewModel,
    profileInfoState: ProfileInfo?,
    onSetBottomBarVisible: (Boolean) -> Unit,
    onSetCurrentScreen: (Dest) -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    redeemList: List<RedeemInfo>
) {
    val onGoingOrderListState = coffeeViewModel.onGoingOrderList.collectAsState()
    val orderHistoryListState = coffeeViewModel.historyOrderList.collectAsState()
    val context = navController.context

    var backPressedOnce by rememberSaveable { mutableStateOf(false) }

    // Timer reset after delay
    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000) // 2 seconds window
            backPressedOnce = false
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(padding)
    ) {
        composable(Home.route) {
            BackHandler {
                if (navController.previousBackStackEntry != null) {
                    handleBackNavigation(
                        navController,
                        onSetBottomBarVisible,
                        onSetCurrentScreen
                    )
                } else {
                    if (backPressedOnce) {
                        (context as? Activity)?.finish()
                    } else {
                        backPressedOnce = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Press back again to exit")
                        }
                    }
                }
            }

            HomeScreen(
                coffeeCnt = profileInfoState?.coffeeCnt ?: 0,
                username = profileInfoState?.name ?: "",
                onProfileClick = {
                    onSetBottomBarVisible(false)
                    navController.navigateSingleTopTo(Profile.route)
                },
                onCartClick = {
                    onSetBottomBarVisible(false)
                    navController.navigateSingleTopTo(Cart.route)
                },
                onItemClick = { coffeeType ->
                    onSetBottomBarVisible(false)
                    navController.navigateSingleTopTo(CoffeeDetail.route + "/${coffeeType.text}/false")
                },
                modifier = Modifier.padding(padding),
                onRedeemClick = {
                    coffeeViewModel.updateLoyaltyCoffeeCount(
                        coffeeViewModel.coffeeCnt.value - 8
                    )
                    sendNotification(
                        context,
                        "Loyalty Coffee Redeemed",
                        "You have redeemed a loyalty coffee! Enjoy your next cup.",
                    )
                },
                coffeeTypeList = coffeeViewModel.coffeeTypeList
            )
        }
        composable(Reward.route) {
            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }
            RewardScreen(
                onRedeemReward = {
                    onSetBottomBarVisible(false)
                    navController.navigateSingleTopTo(Redeem.route)
                },
                modifier = Modifier.padding(padding),
                coffeeViewModel,
                onRedeemLoyalty = {
                    coffeeViewModel.updateLoyaltyCoffeeCount(
                        coffeeViewModel.coffeeCnt.value - 8
                    )
                    sendNotification(
                        context,
                        "Loyalty Coffee Redeemed",
                        "You have redeemed a loyalty coffee! Enjoy your next cup.",
                    )
                }
            )
        }
        composable(
            route = "${Orders.route}?status={status}",
            arguments = listOf(
                navArgument("status") {
                    type = NavType.StringType
                    defaultValue = "ongoing" // optional, can be "ongoing" or "completed"
                }
            )
        ) {
            val status = it.arguments?.getString("status") ?: "ongoing"

            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }
            OrderScreen(
                onGivenOrder = { orderInfo ->
                    coffeeViewModel.moveToCompletedOrders(orderInfo)
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("navigateTo", Orders.route)
                        putExtra("status", "completed") // if needed
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    sendNotification(
                        context,
                        "Order Completed",
                        "Your order for ${context.getString(orderInfo.coffeeType.text)} has been completed!" +
                                " Go to Orders to track it.",
                        pendingIntent
                    )
                },
                onGoingOrderList = onGoingOrderListState.value,
                orderHistoryList = orderHistoryListState.value,
                status = status,
                modifier = Modifier.padding(padding)
            )
        }
        composable(Profile.route) {
            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }
            ProfileScreen(
                modifier = Modifier.padding(padding),
                onBackButton = {
                    onSetBottomBarVisible(true)
                    navController.navigateUp()
                },
                coffeeViewModel
            )
        }
        composable(Redeem.route) {
            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }
            RedeemScreen(
                redeemList = redeemList,
                onBackButton = {
                    // isShowingBottomBar = true
                    // navController.navigateUp()
                    handleBackNavigation(
                        navController,
                        onSetBottomBarVisible,
                        onSetCurrentScreen
                    )
                },
                onRedeem = { coffeeType, redeemInfo ->
                    val point = coffeeViewModel.redeemPoint
                    if (point.value >= redeemInfo.pointsRequired) {
                        coffeeViewModel.updateRedeemPoint(
                            point.value - redeemInfo.pointsRequired
                        )
                        onSetBottomBarVisible(false)
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

            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }

            CoffeeDetailScreen(
                isRedeemed = isRedeem,
                coffeeData = coffeeData,
                coffeeDetailDataState = coffeeDetail,
                onBackButton = {
                    // val previousDestination =
                    //     navController.previousBackStackEntry?.destination?.route
                    // isShowingBottomBar =
                    //     previousDestination in listOf(Home.route, Reward.route, Orders.route)
                    // currentScreen = when (previousDestination) {
                    //     Home.route -> Home
                    //     Reward.route -> Reward
                    //     Orders.route -> Orders
                    //     else -> Home // Default to Home if no previous destination matches
                    // }
                    // navController.navigateUp()
                    handleBackNavigation(
                        navController,
                        onSetBottomBarVisible,
                        onSetCurrentScreen
                    )
                },
                onAddToCart = {
                    onSetBottomBarVisible(false)
                    coffeeViewModel.addCoffeeOrderItem(
                        isRedeemed = isRedeem,
                        coffeeData ?: CoffeeTypeData(
                            drawable = R.drawable.ic_coffee,
                            text = 0, // Fallback in case of null
                        ),
                        coffeeDetail
                    )
                    navController.navigate(Cart.route) {
                        popUpTo(CoffeeDetail.route) {
                            inclusive = true
                        } // This removes CoffeeDetail from backstack
                        launchSingleTop = true
                    }
                },
                onCheckOut = {
                    onSetBottomBarVisible(false)
                    coffeeViewModel.buy()
                    navController.navigateSingleTopTo(OrderSuccess.route)
                    createNotificationChannel(context)

                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("navigateTo", Orders.route)
                        putExtra("status", "ongoing") // if needed
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    sendNotification(
                        context,
                        "Order Placed",
                        "Your coffee order has been placed successfully! Go to Orders to track it.",
                        pendingIntent
                    )
                },
                coffeeBuyList = coffeeViewModel.coffeeBuyList.toMutableStateList(),
                modifier = Modifier.padding(padding)
            )
        }
        composable(Cart.route) {
            val cartItems = coffeeViewModel.coffeeBuyList

            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }

            CartScreen(
                cartItems = cartItems,
                onBackButton = {
                    onSetBottomBarVisible(true)
                    navController.navigateSingleTopTo(Home.route)
                },
                onCheckoutClick = {
                    onSetBottomBarVisible(false)
                    coffeeViewModel.buy()
                    navController.navigateSingleTopTo(OrderSuccess.route)
                    createNotificationChannel(context)

                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("navigateTo", Orders.route)
                        putExtra("status", "ongoing") // if needed
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    sendNotification(
                        context,
                        "Order Placed",
                        "Your coffee order has been placed successfully! Go to Orders to track it.",
                        pendingIntent
                    )

                },
                modifier = Modifier.padding(padding),
                onDeleteItem = { buyItem ->
                    coffeeViewModel.removeCoffeeOrderItem(buyItem) // Remove item from cart
                }
            )
        }
        composable(OrderSuccess.route) {
            BackHandler {
                handleBackNavigation(
                    navController,
                    onSetBottomBarVisible,
                    onSetCurrentScreen
                )
            }
            OrderSuccessScreen(
                onTrackOrderClick = {
                    onSetBottomBarVisible(true)
                    onSetCurrentScreen(Orders)
                    navController.navigateSingleTopTo(Orders.route)
                },
            )
        }
    }
}