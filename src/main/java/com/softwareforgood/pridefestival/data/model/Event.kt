package com.softwareforgood.pridefestival.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.parse.ParseFile
import com.parse.ParseGeoPoint
import com.softwareforgood.pridefestival.data.room.RoomDataConverter
import org.threeten.bp.ZonedDateTime

@Entity
@TypeConverters(RoomDataConverter::class)
data class Event(
    @PrimaryKey
    override val objectId: String,
    val locationName: String? = null,
    val details: String? = null,
    val verified: Boolean,
    val name: String,
    override val location: ParseGeoPoint? = null,
    val startTime: ZonedDateTime? = null,
    val type: EventType = EventType.MISCELLANEOUS,
    val image: ParseFile? = null,
    val website: String? = null,
    val favorite: Boolean = false
) : Mappable, HasParseId, HasGeoLocation

enum class EventType(val parseText: String) {
    PERFORMANCE("performance"),
    MUSIC("music"),
    SPORTS("sports"),
    FOOD("food"),
    MISCELLANEOUS("");

    companion object {
        fun fromParseText(parseText: String?) =
                values().find { it.parseText == parseText } ?: MISCELLANEOUS
    }
}

