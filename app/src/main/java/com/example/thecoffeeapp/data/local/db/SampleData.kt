package com.example.thecoffeeapp.data.local.db

import com.example.thecoffeeapp.ui.screens.CoffeeTypeData
import com.example.thecoffeeapp.R
import com.example.thecoffeeapp.data.local.entity.ProfileInfo
import com.example.thecoffeeapp.data.local.entity.RedeemInfo
import java.time.LocalDate

val point = 0
val coffeeCnt = 0

val sampleProfileInfo = ProfileInfo(
    name = "Tu Thanh",
    email = "tuthanh10825@gmail.com",
    phone = "+1 915 123 456",
    address = "3 Addersion Court Chino Hills, HO56824, United State",

    coffeeCnt = coffeeCnt,
    redeemPoint = point
)



val sampleCoffeeTypes = listOf(
    CoffeeTypeData(R.drawable.type1_americano, R.string.type1_americano),
    CoffeeTypeData(R.drawable.type2_cappucino, R.string.type2_cappuccino),
    CoffeeTypeData(R.drawable.type3_mocha, R.string.type3_mocha),
    CoffeeTypeData(R.drawable.type4_flatwhite, R.string.type4_flatwhite),
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
