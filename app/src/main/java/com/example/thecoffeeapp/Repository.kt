package com.example.thecoffeeapp

import com.example.thecoffeeapp.data.dao.OrderDao
import com.example.thecoffeeapp.data.dao.ProfileDao
import com.example.thecoffeeapp.data.RepositoryInterface
import com.example.thecoffeeapp.data.dao.RewardDao
import com.example.thecoffeeapp.data.entity.OrderInfo
import com.example.thecoffeeapp.data.entity.ProfileInfo
import com.example.thecoffeeapp.data.entity.RewardHistory
import kotlinx.coroutines.flow.Flow

class Repository(
    private val profileDao: ProfileDao,
    private val orderDao: OrderDao,
    private val rewardDao: RewardDao
) : RepositoryInterface {

    val profile: Flow<ProfileInfo?> = profileDao.getProfile()
    val rewardList : Flow<List<RewardHistory>> = rewardDao.getRewards()
    val onGoingOrders: Flow<List<OrderInfo>> = orderDao.getOngoingOrders()
    val completedOrders: Flow<List<OrderInfo>> = orderDao.getCompletedOrders()

    // Profile related operations
    override suspend fun saveProfile(profile: ProfileInfo) = profileDao.insert(profile)

    fun getAllOrders(): Flow<List<OrderInfo>> {
        return orderDao.getAllOrders()
    }

    // Order related operations
    override suspend fun insertOrder(order: OrderInfo) = orderDao.insertOrder(order)
    override suspend fun updateOrder(order: OrderInfo) = orderDao.updateOrder(order)
    override suspend fun deleteOrder(orderId: Int) = orderDao.deleteOrder(orderId)


    override suspend fun insertReward(reward: RewardHistory) = rewardDao.insert(reward)
    override suspend fun getTotalPoints() = rewardDao.getTotalPoints()

}