package com.softwareforgood.pridefestival.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softwareforgood.pridefestival.data.model.Event
import com.softwareforgood.pridefestival.data.model.ParadeEvent
import com.softwareforgood.pridefestival.data.model.Vendor

@Database(
    entities = [Event::class, ParadeEvent::class, Vendor::class],
    version = 1,
    exportSchema = false
)
abstract class PrideDatabase: RoomDatabase() {
    abstract fun prideDao(): PrideDao
}