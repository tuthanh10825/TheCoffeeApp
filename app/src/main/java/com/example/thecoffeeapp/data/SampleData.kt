package com.example.thecoffeeapp.data

import com.example.thecoffeeapp.CoffeeTypeData
import com.example.thecoffeeapp.OrderInfo
import com.example.thecoffeeapp.ProfileInfo
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.RedeemInfo
import com.example.thecoffeeapp.RewardHistory
import java.time.LocalDate
import java.time.LocalDateTime

val point = 2750

val sampleProfileInfo = ProfileInfo(
    name = "Tu Thanh",
    email = "tuthanh10825@gmail.com",
    phone = "+1 915 123 456",
    address = "3 Addersion Court Chino Hills, HO56824, United State"
)



val sampleCoffeeTypes = listOf(
    CoffeeTypeData(R.drawable.type1_americano, R.string.type1_americano),
    CoffeeTypeData(R.drawable.type2_cappucino, R.string.type2_cappuccino),
    CoffeeTypeData(R.drawable.type3_mocha, R.string.type3_mocha),
    CoffeeTypeData(R.drawable.type4_flatwhite, R.string.type4_flatwhite),
)

val sampleOrders = listOf(
    OrderInfo(
        coffeeType = sampleCoffeeTypes[2],
        address = "3 Addersion Court Chino Hills, HO56824, United State",
        cost = 3.00f,
        orderTime = LocalDateTime.of(2025, 6, 24, 12, 30)
    ),
    OrderInfo(
        coffeeType = sampleCoffeeTypes.get(1),
        address = "3 Addersion Court Chino Hills, HO56824, United State",
        cost = 3.00f,
        orderTime = LocalDateTime.of(2025, 6, 24, 12, 30)
    ),
    OrderInfo(
        coffeeType = sampleCoffeeTypes.get(3),
        address = "3 Addersion Court Chino Hills, HO56824, United State",
        cost = 3.00f,
        orderTime = LocalDateTime.of(2025, 6, 24, 12, 30)
    )
)

val sampleHist = listOf(
    OrderInfo(
        coffeeType = sampleCoffeeTypes[0],
        address = "12 Maple Street, San Francisco, CA 94102, United States",
        cost = 4.25f,
        orderTime = LocalDateTime.of(2025, 6, 25, 9, 15)
    ),
    OrderInfo(
        coffeeType = sampleCoffeeTypes[1],
        address = "44 King Road, Austin, TX 73301, United States",
        cost = 2.50f,
        orderTime = LocalDateTime.of(2025, 6, 25, 10, 45)
    ),
    OrderInfo(
        coffeeType = sampleCoffeeTypes[2],
        address = "87 Ocean Ave, Miami, FL 33101, United States",
        cost = 4.75f,
        orderTime = LocalDateTime.of(2025, 6, 26, 8, 5)
    ),
    OrderInfo(
        coffeeType = sampleCoffeeTypes[3],
        address = "291 Central Park West, New York, NY 10024, United States",
        cost = 3.95f,
        orderTime = LocalDateTime.of(2025, 6, 26, 14, 10)
    )
)



val sampleRewards = listOf(
    RewardHistory(sampleCoffeeTypes[1], LocalDateTime.of(2025, 6, 24, 12, 30), 12),
    RewardHistory(sampleCoffeeTypes[2], LocalDateTime.of(2025, 6, 22, 8, 30), 12),
    RewardHistory(sampleCoffeeTypes[3], LocalDateTime.of(2025, 6, 16, 10, 48), 12),
    RewardHistory(sampleCoffeeTypes[0], LocalDateTime.of(2025, 5, 12, 11, 25), 12)
)

val sampleRedeemList = listOf(
    RedeemInfo(
        type = sampleCoffeeTypes[1],
        validDate = LocalDate.of(2021, 7, 4),
        pointsRequired = 1340,
    ),
    RedeemInfo(
        type = sampleCoffeeTypes[2],
        validDate = LocalDate.of(2021, 7, 5),
        pointsRequired = 1500,
    ),
    RedeemInfo(
        type = sampleCoffeeTypes[3],
        validDate = LocalDate.of(2021, 7, 6),
        pointsRequired = 1200,
    )
)
