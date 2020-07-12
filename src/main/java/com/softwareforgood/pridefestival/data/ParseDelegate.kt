package com.softwareforgood.pridefestival.data

import com.parse.ParseObject
import com.parse.ParseQuery
import com.softwareforgood.pridefestival.data.model.Event
import com.softwareforgood.pridefestival.data.model.ParadeEvent
import com.softwareforgood.pridefestival.data.model.Vendor
import com.softwareforgood.pridefestival.util.toEvent
import com.softwareforgood.pridefestival.util.toParadeEvent
import com.softwareforgood.pridefestival.util.toVendor
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/** @see ParseQuery.setLimit */
private const val MAX_LIMIT = 1000

/**
 * Delegate to the static parse methods in order to make things testable
 */
interface ParseDelegate {
    val eventsFromNetwork: Single<List<ParseObject>>
    fun getEventFromNetwork(objectId: String): Single<Event>
    val paradeFromNetwork: Single<List<ParseObject>>
    fun getParadeFromNetwork(objectId: String): Single<ParadeEvent>
    val vendorsFromNetwork: Single<List<ParseObject>>
    fun getVendorFromNetwork(objectId: String): Single<Vendor>
}

@Reusable
class DefaultParseDelegate @Inject constructor() : ParseDelegate {
    override val eventsFromNetwork: Single<List<ParseObject>> = Single.fromCallable {
        eventQuery.find()
    }.timeout(3, TimeUnit.SECONDS)

    override fun getEventFromNetwork(objectId: String): Single<Event> = Single.fromCallable {
        eventQuery.get(objectId)
                .toEvent()
    }.timeout(1, TimeUnit.SECONDS)

    override val paradeFromNetwork: Single<List<ParseObject>> = Single.fromCallable {
        paradeQuery.find()
    }.timeout(3, TimeUnit.SECONDS)

    override fun getParadeFromNetwork(objectId: String): Single<ParadeEvent> = Single.fromCallable {
        paradeQuery.fromLocalDatastore()
                .get(objectId)
                .toParadeEvent()
    }.timeout(1, TimeUnit.SECONDS)

    override val vendorsFromNetwork: Single<List<ParseObject>> = Single.fromCallable {
        vendorQuery.find()
    }.timeout(3, TimeUnit.SECONDS)

    override fun getVendorFromNetwork(objectId: String): Single<Vendor> = Single.fromCallable {
        vendorQuery.get(objectId).toVendor()
    }.timeout(1, TimeUnit.SECONDS)

    private inline val eventQuery: ParseQuery<ParseObject> get() =
        ParseQuery.getQuery<ParseObject>("Event")
            .noQueryLimit()

    private inline val paradeQuery: ParseQuery<ParseObject> get() =
        ParseQuery.getQuery<ParseObject>("ParadeEvent")
            .noQueryLimit()

    private inline val vendorQuery: ParseQuery<ParseObject> get() =
        ParseQuery.getQuery<ParseObject>("Vendor")
            .noQueryLimit()

    private fun <T : ParseObject> ParseQuery<T>.noQueryLimit() = setLimit(MAX_LIMIT)
}
