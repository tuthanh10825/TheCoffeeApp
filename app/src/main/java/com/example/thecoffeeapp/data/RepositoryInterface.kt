package com.example.thecoffeeapp.data

import com.example.thecoffeeapp.data.entity.OrderInfo
import com.example.thecoffeeapp.data.entity.ProfileInfo
import com.example.thecoffeeapp.data.entity.RewardHistory
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    // Profile related operations
    suspend fun saveProfile(profile: ProfileInfo)

    // Order related operations
    suspend fun insertOrder(order: OrderInfo)
    suspend fun updateOrder(order: OrderInfo)
    suspend fun deleteOrder(orderId: Int)

    // Reward related operations
    suspend fun insertReward(reward: RewardHistory)
    suspend fun getTotalPoints(): Flow<Int>
}