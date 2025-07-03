package com.example.thecoffeeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.thecoffeeapp.CoffeeTypeData
import java.time.LocalDate
import java.time.LocalDateTime


@Entity(tableName = "orders")
data class OrderInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coffeeType: CoffeeTypeData,
    val address: String,
    val cost: Float,
    val orderTime: LocalDateTime,

    // 0 for on-going, 1 for done
    val status: Boolean
)

@Entity(tableName = "rewards")
data class RewardHistory(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val type: CoffeeTypeData,
    val dateTime: LocalDateTime,
    val points: Int
)

@Entity(tableName = "profile")
data class ProfileInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,

    val coffeeCnt: Int,
    val redeemPoint: Int,

)

data class RedeemInfo(
    val type: CoffeeTypeData,
    val validDate: LocalDate,
    val pointsRequired: Int,
)


