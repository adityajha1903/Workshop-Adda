package com.example.workshopadda.db

import androidx.room.Dao
import androidx.room.Query
import com.example.workshopadda.models.Workshop
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkshopDao {

    @Query("select * from `workshopTable`")
    suspend fun fetchAllPlaces(): List<Workshop>
}