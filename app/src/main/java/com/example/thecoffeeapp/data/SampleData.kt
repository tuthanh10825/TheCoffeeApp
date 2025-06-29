package com.example.thecoffeeapp.data

import com.example.thecoffeeapp.OrderInfo
import com.example.thecoffeeapp.RewardHistory
import java.time.LocalDateTime

val sampleOrders = listOf(
    OrderInfo(
        coffeeType = "Americano",
        address = "3 Addersion Court Chino Hills, HO56824, United State",
        cost = 3.00f,
        orderTime = LocalDateTime.of(2025, 6, 24, 12, 30)
    ),
    OrderInfo(
        coffeeType = "Cafe Latte",
        address = "3 Addersion Court Chino Hills, HO56824, United State",
        cost = 3.00f,
        orderTime = LocalDateTime.of(2025, 6, 24, 12, 30)
    ),
    OrderInfo(
        coffeeType = "Flat White",
        address = "3 Addersion Court Chino Hills, HO56824, United State",
        cost = 3.00f,
        orderTime = LocalDateTime.of(2025, 6, 24, 12, 30)
    )
)

val sampleHist = listOf(
    OrderInfo(
        coffeeType = "Cappuccino",
        address = "12 Maple Street, San Francisco, CA 94102, United States",
        cost = 4.25f,
        orderTime = LocalDateTime.of(2025, 6, 25, 9, 15)
    ),
    OrderInfo(
        coffeeType = "Espresso",
        address = "44 King Road, Austin, TX 73301, United States",
        cost = 2.50f,
        orderTime = LocalDateTime.of(2025, 6, 25, 10, 45)
    ),
    OrderInfo(
        coffeeType = "Mocha",
        address = "87 Ocean Ave, Miami, FL 33101, United States",
        cost = 4.75f,
        orderTime = LocalDateTime.of(2025, 6, 26, 8, 5)
    ),
    OrderInfo(
        coffeeType = "Cold Brew",
        address = "291 Central Park West, New York, NY 10024, United States",
        cost = 3.95f,
        orderTime = LocalDateTime.of(2025, 6, 26, 14, 10)
    ),
    OrderInfo(
        coffeeType = "Macchiato",
        address = "6 Green Lane, Seattle, WA 98101, United States",
        cost = 3.80f,
        orderTime = LocalDateTime.of(2025, 6, 27, 16, 50)
    )
)

val point = 2750

val sampleRewards = listOf(
    RewardHistory("Americano", LocalDateTime.of(2025, 6, 24, 12, 30), 12),
    RewardHistory("Cafe Latte", LocalDateTime.of(2025, 6, 22, 8, 30), 12),
    RewardHistory("Green Tea Latte", LocalDateTime.of(2025, 6, 16, 10, 48), 12),
    RewardHistory("Flat White", LocalDateTime.of(2025, 5, 12, 11, 25), 12)
)

