package com.adiandrodev.workshopadda.data.db

import com.adiandrodev.workshopadda.data.model.Workshop

class WorkshopRepository(private val workshopDao: WorkshopDao) {
    suspend fun getAllWorkshops(): List<Workshop> {
        return workshopDao.fetchAllWorkshop()
    }
}