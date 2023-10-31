package com.adiandrodev.workshopadda.data.db

import androidx.room.Dao
import androidx.room.Query
import com.adiandrodev.workshopadda.data.model.Workshop

@Dao
interface WorkshopDao {

    @Query("select * from `workshopTable`")
    suspend fun fetchAllWorkshop(): List<Workshop>
}