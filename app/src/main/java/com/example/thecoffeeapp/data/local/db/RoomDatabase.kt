package com.example.thecoffeeapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.thecoffeeapp.data.local.dao.OrderDao
import com.example.thecoffeeapp.data.local.dao.ProfileDao
import com.example.thecoffeeapp.data.local.dao.RewardDao
import com.example.thecoffeeapp.data.local.entity.OrderInfo
import com.example.thecoffeeapp.data.local.entity.ProfileInfo
import com.example.thecoffeeapp.data.local.entity.RewardHistory

@Database(
    entities = [OrderInfo::class, RewardHistory::class, ProfileInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CoffeeRoomDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun orderDao(): OrderDao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeRoomDatabase? = null

        fun getDatabase(context: Context): CoffeeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeRoomDatabase::class.java,
                    "coffee_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }

}