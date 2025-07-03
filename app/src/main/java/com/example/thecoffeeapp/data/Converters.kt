package com.example.thecoffeeapp.data

import androidx.room.TypeConverter
import com.example.thecoffeeapp.ui.screens.CoffeeTypeData
import com.google.gson.Gson
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): String = value.toString()

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)

    @TypeConverter
    fun fromCoffeeType(value: CoffeeTypeData): String = Gson().toJson(value)

    @TypeConverter
    fun toCoffeeType(value: String): CoffeeTypeData = Gson().fromJson(value, CoffeeTypeData::class.java)
}