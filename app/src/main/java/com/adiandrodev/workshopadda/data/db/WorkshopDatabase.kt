package com.adiandrodev.workshopadda.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adiandrodev.workshopadda.data.model.Workshop

@Database(entities = [Workshop::class], version = 1)
abstract class WorkshopDatabase: RoomDatabase() {

    abstract fun workshopDao(): WorkshopDao

    companion object {
        @Volatile
        private var INSTANCE: WorkshopDatabase? = null
        private var WORKSHOP_DATABASE = "workshopTable"
        fun getInstance(context: Context): WorkshopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkshopDatabase::class.java,
                    WORKSHOP_DATABASE
                )
                    .createFromAsset("db/workshopTable.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}