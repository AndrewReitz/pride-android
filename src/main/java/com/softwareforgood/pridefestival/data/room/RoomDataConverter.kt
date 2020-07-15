package com.softwareforgood.pridefestival.data.room

import androidx.room.TypeConverter
import com.parse.ParseFile
import com.parse.ParseGeoPoint
import com.softwareforgood.pridefestival.PrideApp
import com.softwareforgood.pridefestival.data.model.EventType
import com.softwareforgood.pridefestival.data.model.VendorColor
import com.softwareforgood.pridefestival.data.model.VendorType
import org.threeten.bp.Instant
import org.threeten.bp.ZonedDateTime

class RoomDataConverter {
    @TypeConverter
    fun toVendorType(data: String): VendorType = VendorType.fromParseText(data)

    @TypeConverter
    fun fromVendorType(data: VendorType): String = data.parseText

    @TypeConverter
    fun toVendorColor(data: String): VendorColor = VendorColor.fromColorString(data)

    @TypeConverter
    fun fromVendorColor(data: VendorColor): String = data.name

    @TypeConverter
    fun toEventType(data: String): EventType =
        EventType.fromParseText(
            data
        )

    @TypeConverter
    fun fromEventType(data: EventType): String = data.parseText

    @TypeConverter
    fun toParseFile(data: ByteArray?): ParseFile? =
        if (data == null) null else ParseFile(data)

    @TypeConverter
    fun fromParseFile(data: ParseFile?): ByteArray? = data?.data

    @TypeConverter
    fun toZonedDateTime(data: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(
                data
            ),
            PrideApp.CENTRAL_TIMEZONE
        )
    }

    @TypeConverter
    fun fromZonedDateTime(data: ZonedDateTime): Long = data.toInstant().toEpochMilli()

    @TypeConverter
    fun toParseGeoPoint(data: String?): ParseGeoPoint? {
        if (data == null) return null
        val (lat, long) = data.split(",")
        return ParseGeoPoint(lat.toDouble(), long.toDouble())
    }

    @TypeConverter
    fun fromParseGeoPoint(data: ParseGeoPoint?): String? {
        if (data == null) return null
        return "${data.latitude},${data.longitude}"
    }
}