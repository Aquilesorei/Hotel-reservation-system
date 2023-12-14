package com.aquiles.hotel.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.aquiles.hotel.data.HotDao
import com.aquiles.hotel.local.LocalNews
import com.aquiles.hotel.models.Client
import com.aquiles.hotel.models.Profile
import com.aquiles.hotel.models.Reservation
import com.aquiles.hotel.models.Room


@Database(
    entities = [Profile::class,Reservation::class, Room::class,Client::class, LocalNews::class],
    version = 6,
    exportSchema = false
)

abstract class HotDb : RoomDatabase(){
    abstract  val dao : HotDao
    companion object {
        @Volatile
        var  INSTANCE : HotDao? = null
        fun getDaoInstance(context: Context) : HotDao
        {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }
                return instance
            }
        }


        private fun buildDatabase(context: Context):
                HotDb =
           databaseBuilder(
                context.applicationContext,
                HotDb::class.java,
                "hotel_database")
                .fallbackToDestructiveMigration()
                .build()
    }

}