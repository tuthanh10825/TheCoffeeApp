package com.example.thecoffeeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.thecoffeeapp.data.dao.OrderDao
import com.example.thecoffeeapp.data.dao.ProfileDao
import com.example.thecoffeeapp.data.dao.RewardDao
import com.example.thecoffeeapp.data.entity.OrderInfo
import com.example.thecoffeeapp.data.entity.ProfileInfo
import com.example.thecoffeeapp.data.entity.RewardHistory

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