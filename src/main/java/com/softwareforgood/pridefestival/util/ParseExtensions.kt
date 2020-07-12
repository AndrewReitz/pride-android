package com.softwareforgood.pridefestival.util

import com.google.android.gms.maps.model.LatLng
import com.parse.ParseFile
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import com.softwareforgood.pridefestival.PrideApp.Companion.CENTRAL_TIMEZONE
import com.softwareforgood.pridefestival.data.model.Event
import com.softwareforgood.pridefestival.data.model.EventType
import com.softwareforgood.pridefestival.data.model.ParadeEvent
import com.softwareforgood.pridefestival.data.model.Vendor
import com.softwareforgood.pridefestival.data.model.VendorType
import com.softwareforgood.pridefestival.data.model.VendorColor
import io.reactivex.Single
import org.threeten.bp.Instant
import org.threeten.bp.ZonedDateTime
import java.io.File

/**
 * Retrieves a [ZonedDateTime] from parse.
 */
fun ParseObject.getZonedDateTime(key: String): ZonedDateTime? {
    val time = this.getDate(key)?.time ?: return null
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), CENTRAL_TIMEZONE)
}

fun ParseFile.toSingle(): Single<ByteArray> {
    return Single.create<ByteArray> { emitter ->
        getDataInBackground { data, error ->
            when {
                error != null -> emitter.onError(error)
                else -> emitter.onSuccess(data)
            }
        }
    }
}

fun ParseGeoPoint.toLatLng() = LatLng(latitude, longitude)

fun ParseObject.toEvent() = Event(
        objectId = objectId,
        locationName = getString("locationName"),
        details = getString("details"),
        verified = getBoolean("verified"),
        name = getString("name") ?: "",
        location = getParseGeoPoint("location"),
        startTime = getZonedDateTime("startTime"),
        type = EventType.fromParseText(getString("type")),
        image = getParseFile("image"),
        website = getString("website")
)

fun ParseObject.toParadeEvent() = ParadeEvent(
        objectId = objectId,
        name = getString("name") ?: "",
        details = getString("details"),
        lineupNumber = getInt("lineupNumber"),
        verified = getBoolean("verified")
)

fun ParseObject.toVendor() = Vendor(
        objectId = objectId,
        locationName = getString("locationName"),
        showInParadeList = getBoolean("showInParadeList"),
        details = getString("details"),
        verified = getBoolean("verified"),
        name = getString("name") ?: "",
        location = getParseGeoPoint("location"),
        website = getString("website"),
        vendorType = VendorType.fromParseText(getString("vendorType")),
        logo = getParseFile("logo"),
        isSponsor = getBoolean("isSponsor"),
        sectionColor = VendorColor.fromColorString(getString("sectionColor"))
)
