package com.softwareforgood.pridefestival

import com.f2prateek.rx.preferences2.Preference
import com.parse.ParseObject
import com.parse.ParseQuery
import com.softwareforgood.pridefestival.data.*
import com.softwareforgood.pridefestival.data.room.PrideDao
import com.softwareforgood.pridefestival.util.toEvent
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Calls each loader which will cache data to the local database and be used. This will
 * ensure all data is there if offline mode is used.
 */
class FirstRunCachingService @Inject constructor(
    private val eventsLoader: EventsLoader,
    private val infoLoader: InfoLoader,
    private val paradeLoader: ParadeLoader,
    private val vendorLoader: VendorLoader,
    @FirstRunPref private val firstRunPref: Preference<Boolean>,
    private val prideDao: PrideDao
) {
    private inline val eventQuery: ParseQuery<ParseObject>
        get() = ParseQuery.getQuery<ParseObject>("Event").run { setLimit(Int.MAX_VALUE) }

    private inline val paradeQuery: ParseQuery<ParseObject>
        get() = ParseQuery.getQuery<ParseObject>("ParadeEvent").run { setLimit(Int.MAX_VALUE) }

    private inline val vendorQuery: ParseQuery<ParseObject>
        get() = ParseQuery.getQuery<ParseObject>("Vendor").run { setLimit(Int.MAX_VALUE) }

    fun cacheParseObjects(): Disposable {
        GlobalScope.launch {
            prideDao.clearDatabase()

            val events = eventQuery.find()
                .map { it.toEvent() }

            prideDao.insertEvents(events)
        }

        firstRunPref.get() == true || return Disposables.empty()

        val events = eventsLoader.events
            .doOnError { Timber.e(it, "Error loading events") }
            .retry(3)

        val info = infoLoader.infoText
            .doOnError { Timber.e(it, "Error loading info") }
            .retry(3)

        val parade = paradeLoader.parades
            .doOnError { Timber.e(it, "Error loading parades") }
            .retry(3)

        val vendor = vendorLoader.vendors
            .doOnError { Timber.e(it, "Error loading vendors") }
            .retry(3)

        return Single.mergeDelayError(events, info, parade, vendor)
            .subscribe({
                firstRunPref.set(false)
            }, { Timber.e(it, "Error loading data on first run") })
    }
}
