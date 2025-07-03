package com.example.thecoffeeapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.data.local.entity.ProfileInfo
import com.example.thecoffeeapp.data.local.entity.RedeemInfo
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
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
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
                },
                coffeeTypeList = coffeeViewModel.coffeeTypeList
            )
        }
        composable(Reward.route) {
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
            ProfileScreen(
                modifier = Modifier.padding(padding),
                onBackButton = {
                    onSetBottomBarVisible(false)
                    navController.navigateUp()
                },
                coffeeViewModel
            )
        }
        composable(Redeem.route) {
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
                coffeeBuyList = coffeeViewModel.coffeeBuyList.toMutableStateList(),
                modifier = Modifier.padding(padding)
            )
        }
        composable(Cart.route) {
            val cartItems = coffeeViewModel.coffeeBuyList
            CartScreen(
                cartItems = cartItems,
                onBackButton = {
                    val previousDestination =
                        navController.previousBackStackEntry?.destination?.route
                    onSetBottomBarVisible(previousDestination == Home.route)
                    navController.navigateUp()
                },
                onCheckoutClick = {
                    onSetBottomBarVisible(false)
                    coffeeViewModel.buy()
                    navController.navigateSingleTopTo(OrderSuccess.route)
                },
                modifier = Modifier.padding(padding),
                onDeleteItem = { buyItem ->
                    coffeeViewModel.removeCoffeeOrderItem(buyItem) // Remove item from cart
                }
            )
        }
        composable(OrderSuccess.route) {
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