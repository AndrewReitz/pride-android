package com.softwareforgood.pridefestival

import com.parse.ParseObject
import com.parse.ParseQuery
import com.softwareforgood.pridefestival.data.room.PrideDao
import com.softwareforgood.pridefestival.util.toEvent
import com.softwareforgood.pridefestival.util.toParadeEvent
import com.softwareforgood.pridefestival.util.toVendor
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Populates the database for querying later
 */
class FirstRunCachingService @Inject constructor(
    private val prideDao: PrideDao
) {

    private inline val eventQuery: ParseQuery<ParseObject>
        get() = ParseQuery.getQuery<ParseObject>("Event").run { setLimit(Int.MAX_VALUE) }

    private inline val paradeQuery: ParseQuery<ParseObject>
        get() = ParseQuery.getQuery<ParseObject>("ParadeEvent").run { setLimit(Int.MAX_VALUE) }

    private inline val vendorQuery: ParseQuery<ParseObject>
        get() = ParseQuery.getQuery<ParseObject>("Vendor").run { setLimit(Int.MAX_VALUE) }

    fun saveDataFromParse(): Job = CoroutineScope(Dispatchers.IO).launch {
        prideDao.clearDatabase()

        launch {
            val events = eventQuery.find()
                .map { it.toEvent() }

            prideDao.insertEvents(events)
        }

        launch {
            val paradeEvents = paradeQuery.find()
                .map { it.toParadeEvent() }

            prideDao.insertParadeEvent(paradeEvents)
        }

        launch {
            val vendors = vendorQuery.find()
                .map { it.toVendor() }
            prideDao.insertVendors(vendors)
        }
    }
}
