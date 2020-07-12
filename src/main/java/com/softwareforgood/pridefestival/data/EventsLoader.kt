package com.softwareforgood.pridefestival.data

import android.util.Patterns
import com.f2prateek.rx.preferences2.Preference
import com.softwareforgood.pridefestival.data.model.Event
import com.softwareforgood.pridefestival.util.subscribeOnIoScheduler
import com.softwareforgood.pridefestival.util.toEvent
import dagger.Reusable
import io.reactivex.Single
import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import javax.inject.Inject

interface EventsLoader {
    val events: Single<List<Event>>
    fun getEvent(objectId: String): Single<Event>
}

@Reusable
class DefaultEventsLoader @Inject constructor(
    @EventsLastUpdated private val lastUpdated: Preference<Instant>,
    private val updateDuration: Duration,
    private val clock: Clock,
    private val parse: ParseDelegate
) : EventsLoader {
    override val events
        get() = lastUpdated.asObservable()
                .take(1)
                .singleOrError()
                .map { it.plus(updateDuration) }
                .filter { it.isAfter(Instant.now(clock)) }
                .flatMap { eventsFromNetwork.toMaybe() }
                .toObservable()
                .flatMapIterable { it }
                .map { it.toEvent() }
                .map { event ->
                    val website = event.website ?: ""
                    when {
                        website.startsWith("http://") || website.startsWith("https://") -> event
                        Patterns.WEB_URL.matcher(website).matches() -> event.copy(website = "http://")
                        else -> event.copy(website = null)
                    }
                }
                .toList()
                .subscribeOnIoScheduler()


    private val eventsFromNetwork get() = parse.eventsFromNetwork
            .doOnSuccess { lastUpdated.set(Instant.now(clock)) }

    override fun getEvent(objectId: String): Single<Event> =
        parse.getEventFromNetwork(objectId)
            .subscribeOnIoScheduler()
}
