package com.softwareforgood.pridefestival.data

import com.f2prateek.rx.preferences2.Preference
import com.softwareforgood.pridefestival.data.model.ParadeEvent
import com.softwareforgood.pridefestival.util.subscribeOnIoScheduler
import com.softwareforgood.pridefestival.util.toParadeEvent
import dagger.Reusable
import io.reactivex.Single
import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import javax.inject.Inject

interface ParadeLoader {
    val parades: Single<List<ParadeEvent>>
    fun getParade(objectId: String): Single<ParadeEvent>
}

@Reusable
class DefaultParadeLoader @Inject constructor(
    @ParadeLastUpdated private val lastUpdated: Preference<Instant>,
    private val updateDuration: Duration,
    private val clock: Clock,
    private val parse: ParseDelegate
) : ParadeLoader {
    override val parades
        get() = lastUpdated.asObservable()
            .take(1)
            .singleOrError()
            .map { it.plus(updateDuration) }
            .filter { it.isAfter(Instant.now(clock)) }
            .flatMap { paradeFromNetwork.toMaybe() }
            .toObservable()
            .flatMapIterable { it }
            .filter { it.getString("name") != null }
            .map { it.toParadeEvent() }
            .toList()
            .subscribeOnIoScheduler()

    private val paradeFromNetwork
        get() = parse.paradeFromNetwork
            .doOnSuccess { lastUpdated.set(Instant.now(clock)) }

    override fun getParade(objectId: String) = parse.getParadeFromNetwork(objectId)
        .subscribeOnIoScheduler()
}
