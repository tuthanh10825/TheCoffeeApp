package com.example.thecoffeeapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecoffeeapp.data.point
import com.example.thecoffeeapp.data.sampleCoffeeTypes
import com.example.thecoffeeapp.data.sampleProfileInfo
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.thecoffeeapp.data.OrderInfo
import com.example.thecoffeeapp.data.ProfileInfo
import com.example.thecoffeeapp.data.RedeemInfo
import com.example.thecoffeeapp.data.Repository
import com.example.thecoffeeapp.data.RewardHistory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import java.time.LocalDateTime

class CoffeeViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoffeeViewModel(repository) as T
    }
}

// TODO: Convert all sameple data to become an attribute here.
class CoffeeViewModel(
    private val repository: Repository
) : ViewModel() {
    /*Move to the database*/
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

        incrementCoffeeCount()

        for (item in _coffeeBuyList) {
            // Here we can add logic to handle each item in the buy list.
            // For now, we will just print the item.
            println("Bought: ${item.coffeeType.text}")

            val reward = RewardHistory(
                    type = item.coffeeType,
                    dateTime = LocalDateTime.now(),
                    points = 10 // Assuming each coffee purchase gives 10 points
                )
            addReward(reward)
            val currentPoints = userInfo.value?.redeemPoint?:0
            updateRedeemPoint(reward.points + currentPoints)
            viewModelScope.launch {
                repository.insertOrder(
                    OrderInfo(
                        coffeeType = item.coffeeType,
                        address = userInfo.value?.address?: "Unknown Address",
                        cost = item.getPrice().toFloat(),
                        orderTime = LocalDateTime.now(),
                        status = false
                    )
                )
            }
        }

        _coffeeBuyList.clear()
    }

    fun updateOrderStatus(order: OrderInfo, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(status = isCompleted))
        }
    }

    fun updateRedeemPoint(rewardPoints: Int) {
        viewModelScope.launch {
            val profile = userInfo.value
            if(profile != null) {
                repository.saveProfile(
                    profile.copy(
                        redeemPoint = rewardPoints,
                    )
                )
            }
        }
    }

    fun incrementCoffeeCount() {
        viewModelScope.launch {
            val profile = userInfo.value
            if (profile != null) {
                repository.saveProfile(profile.copy(coffeeCnt = profile.coffeeCnt + 1))
            }
        }
    }

    // User info

    /*
    Move to the database
    */

    val userInfo: StateFlow<ProfileInfo?> = repository.profile
                .stateIn(viewModelScope, SharingStarted.Eagerly, null)


    val coffeeCnt: StateFlow<Int> = userInfo.map { it?.coffeeCnt ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val redeemPoint: StateFlow<Int> = userInfo.map { it?.redeemPoint ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val rewardList: StateFlow<List<RewardHistory>> =
        repository.rewardList
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val onGoingOrderList: StateFlow<List<OrderInfo>> =
        repository.onGoingOrders
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val historyOrderList: StateFlow<List<OrderInfo>> =
        repository.completedOrders
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun updateLoyaltyCoffeeCount(newCount: Int) {
        viewModelScope.launch {
            val profile = userInfo.value
            if (profile != null) {
                repository.saveProfile(profile.copy(coffeeCnt = newCount))
            }
        }
    }

    fun addReward(reward: RewardHistory) {
        viewModelScope.launch {
            repository.insertReward(reward)
        }
    }

    fun updateUserInfo(newInfo: ProfileInfo) {
        viewModelScope.launch {
            repository.saveProfile(newInfo)
        }
    }

    fun moveToCompletedOrders(order: OrderInfo) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(status = true))
        }
    }

    fun createTempProfile() {
        if (userInfo.value != null) return // If profile already exists, do nothing
        viewModelScope.launch {
            repository.saveProfile(sampleProfileInfo)
        }
    }
}
