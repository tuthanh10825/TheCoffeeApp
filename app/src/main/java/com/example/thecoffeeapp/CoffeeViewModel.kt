package com.example.thecoffeeapp

import androidx.lifecycle.ViewModel
import com.example.thecoffeeapp.data.sampleCoffeeTypes

class CoffeeViewModel : ViewModel() {
    private var _coffeeTypeList = sampleCoffeeTypes.toMutableList()
    val coffeeTypeList: List<CoffeeTypeData>
        get() = _coffeeTypeList

}
