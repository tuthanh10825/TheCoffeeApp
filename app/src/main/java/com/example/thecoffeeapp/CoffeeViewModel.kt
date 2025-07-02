package com.example.thecoffeeapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.thecoffeeapp.data.point
import com.example.thecoffeeapp.data.sampleCoffeeTypes
import com.example.thecoffeeapp.data.sampleHist
import com.example.thecoffeeapp.data.sampleOrders
import com.example.thecoffeeapp.data.sampleProfileInfo
import com.example.thecoffeeapp.data.sampleRewards
import androidx.compose.runtime.State

// TODO: Convert all sameple data to become an attribute here.
class CoffeeViewModel : ViewModel() {
    private var _coffeeTypeList = sampleCoffeeTypes.toMutableList()
    val coffeeTypeList: List<CoffeeTypeData>
        get() = _coffeeTypeList
    //Probably we add a list of cart items here later.

    fun getCoffeeDataById(textResId: Int): CoffeeTypeData? {
        return _coffeeTypeList.find { it.text == textResId }
    }

    private var _coffeeBuyList = mutableStateListOf<BuyItem>()
    val coffeeBuyList: List<BuyItem>
        get() = _coffeeBuyList

    fun addCoffeeOrderItem(isRedeemed: Boolean, coffeeTypeData: CoffeeTypeData, coffeeDetailDataState: CoffeeDetailDataState) {

        val buyItem = BuyItem(
            isRedeemed,
            coffeeType = coffeeTypeData,
            coffeeDetailData = CoffeeDetailData(
                coffeeDetailDataState.quantity,
                coffeeDetailDataState.shot,
                coffeeDetailDataState.size,
                coffeeDetailDataState.select,
                coffeeDetailDataState.ice,
                )
        )
        _coffeeBuyList.add(buyItem)
    }
    fun removeCoffeeOrderItem(buyItem: BuyItem) {
        _coffeeBuyList.remove(buyItem)
    }

    fun buy() {
        // Here we can add the logic to handle the purchase of coffee items.
        // For now, we will just clear the buy list.

        _coffeeCnt.value += 1 // Or _coffeeBuyList.size
        println("Total coffee count: ${_coffeeCnt.value}")

        for (item in _coffeeBuyList) {
            // Here we can add logic to handle each item in the buy list.
            // For now, we will just print the item.
            println("Bought: ${item.coffeeType.text}")

            _rewardList.add(
                RewardHistory(
                    type = item.coffeeType,
                    dateTime = java.time.LocalDateTime.now(),
                    points = 10 // Assuming each coffee purchase gives 10 points
                )
            )
            _redeemPoint.value += _rewardList.last().points
            _onGoingOrderList.add(
                OrderInfo(
                    coffeeType = item.coffeeType,
                    address = _userInfo.value.address,
                    cost = item.getPrice().toFloat(),
                    orderTime = java.time.LocalDateTime.now()
                )
            )
        }

        _coffeeBuyList.clear()
    }

    fun updateRedeemPoint(redeemInfo: RedeemInfo) {
        _redeemPoint.value -= redeemInfo.pointsRequired
    }

    // User info
    private var _userInfo = mutableStateOf(sampleProfileInfo)
    val userInfo: State<ProfileInfo> get() = _userInfo

    // Redeem point (observable)
    private var _redeemPoint = mutableStateOf(point)
    val redeemPoint: State<Int> get() = _redeemPoint

    // Total coffee count (observable)
    private var _coffeeCnt = mutableStateOf(8)
    val coffeeCnt: State<Int> get() = _coffeeCnt

    // Reward history list (observable)
    private var _rewardList = mutableStateListOf<RewardHistory>().apply {
        addAll(sampleRewards)
    }
    val rewardList: SnapshotStateList<RewardHistory> get() = _rewardList

    // Ongoing orders (observable)
    private var _onGoingOrderList = mutableStateListOf<OrderInfo>().apply {
        addAll(sampleOrders)
    }
    val onGoingOrderList: SnapshotStateList<OrderInfo> get() = _onGoingOrderList

    // History orders (observable)
    private var _historyOrderList = mutableStateListOf<OrderInfo>().apply {
        addAll(sampleHist)
    }
    val historyOrderList: SnapshotStateList<OrderInfo> get() = _historyOrderList

    fun moveToCompletedOrders(order: OrderInfo) {
        _historyOrderList.add(order)
        _onGoingOrderList.remove(order)
    }

    fun updateLoyaltyCoffeeCount(count: Int) {
        _coffeeCnt.value = count
    }

    fun updateUserInfo(info: ProfileInfo) {
        _userInfo.value = info
    }
}
