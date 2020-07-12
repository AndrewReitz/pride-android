package com.softwareforgood.pridefestival.data

import android.util.Patterns
import com.f2prateek.rx.preferences2.Preference
import com.softwareforgood.pridefestival.data.model.Vendor
import com.softwareforgood.pridefestival.util.subscribeOnIoScheduler
import com.softwareforgood.pridefestival.util.toVendor
import dagger.Reusable
import io.reactivex.Single
import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import javax.inject.Inject

interface VendorLoader {
    val vendors: Single<List<Vendor>>
    fun getVendor(objectId: String): Single<Vendor>
}

@Reusable
class DefaultVendorLoader @Inject constructor(
    @VendorLastUpdated private val lastUpdated: Preference<Instant>,
    private val updateDuration: Duration,
    private val clock: Clock,
    private val parse: ParseDelegate
) : VendorLoader {
    override val vendors
        get() = lastUpdated.asObservable()
                .take(1)
                .singleOrError()
                .map { it.plus(updateDuration) }
                .filter { it.isAfter(Instant.now(clock)) }
                .flatMap { vendorsFromNetwork.toMaybe() }
                .toObservable()
                .flatMapIterable { it }
                .map { it.toVendor() }
                .map { vendor ->
                    val website = vendor.website ?: ""
                    when {
                        website.startsWith("http://") || website.startsWith("https://") -> vendor
                        Patterns.WEB_URL.matcher(website).matches() -> vendor.copy(website = "http://")
                        else -> vendor.copy(website = null)
                    }
                }
                .toList()
                .subscribeOnIoScheduler()

    private val vendorsFromNetwork get() = parse.vendorsFromNetwork
            .doOnSuccess { lastUpdated.set(Instant.now(clock)) }

    override fun getVendor(objectId: String) = parse.getVendorFromNetwork(objectId)
            .subscribeOnIoScheduler()
}
