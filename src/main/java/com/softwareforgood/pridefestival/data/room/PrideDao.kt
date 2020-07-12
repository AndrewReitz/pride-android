package com.softwareforgood.pridefestival.data.room

import androidx.room.*
import com.softwareforgood.pridefestival.data.model.Event
import com.softwareforgood.pridefestival.data.model.ParadeEvent
import com.softwareforgood.pridefestival.data.model.Vendor

@Dao
interface PrideDao {
    @Insert fun insertEvents(events: List<Event>)
    @Insert fun insertParadeEvent(paradeEvent: List<ParadeEvent>)
    @Insert fun insertVendor(vendor: List<Vendor>)

    @Query("SELECT * FROM EVENT") suspend fun events(): List<Event>
    @Query("SELECT * FROM PARADEEVENT") suspend fun paradeEvents(): List<ParadeEvent>
    @Query("SELECT * FROM Vendor") suspend fun vendors(): List<Vendor>

    @Delete suspend fun deleteEvents(events: List<Event>)
    @Delete suspend fun deleteParadeEvents(paradeEvent: List<ParadeEvent>)
    @Delete suspend fun deleteVendor(vendors: List<Vendor>)

    @Transaction suspend fun clearDatabase() {
        deleteEvents(events())
        deleteParadeEvents(paradeEvents())
        deleteVendor(vendors())
    }
}