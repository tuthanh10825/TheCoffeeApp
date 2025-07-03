package com.example.thecoffeeapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.thecoffeeapp.data.entity.OrderInfo
import com.example.thecoffeeapp.data.entity.ProfileInfo
import com.example.thecoffeeapp.data.entity.RewardHistory
import kotlinx.coroutines.flow.Flow



@Dao
interface OrderDao {
    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrderInfo>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: Int): Flow<OrderInfo?>

    @Query("SELECT * FROM orders WHERE status = 0") // 0 for on-going
    fun getOngoingOrders(): Flow<List<OrderInfo>>

    @Query("SELECT * FROM orders WHERE status = 1 ") // 1 for done
    fun getCompletedOrders(): Flow<List<OrderInfo>>

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertOrder(order: OrderInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrder(order: OrderInfo)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId: Int)
}

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards")
    fun getRewards(): Flow<List<RewardHistory>>

    @Query("SELECT sum(points) FROM rewards")
    fun getTotalPoints(): Flow<Int>

    @Insert
    suspend fun insert(reward: RewardHistory)
}

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfile(): Flow<ProfileInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ProfileInfo)
}