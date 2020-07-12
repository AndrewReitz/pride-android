package com.softwareforgood.pridefestival.data.model

import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.parse.ParseFile
import com.parse.ParseGeoPoint
import com.softwareforgood.pridefestival.R
import com.softwareforgood.pridefestival.data.room.RoomDataConverter
import java.util.Locale

/**
 * Matches the Vendor Parse Data Table
 */
@Entity
@TypeConverters(RoomDataConverter::class)
data class Vendor(
    @PrimaryKey
    override val objectId: String,
    val locationName: String?,
    val showInParadeList: Boolean,
    val details: String?,
    val verified: Boolean,
    val name: String,
    override val location: ParseGeoPoint?,
    val website: String?,
    val vendorType: VendorType,
    val logo: ParseFile?,
    val isSponsor: Boolean,
    val sectionColor: VendorColor,
    val favorite: Boolean = false
) : Mappable, HasParseId, HasGeoLocation

enum class VendorType(val parseText: String) {
    FOOD("food"),
    NON_FOOD("nonfood"),
    UNKNOWN("");

    companion object {
        fun fromParseText(text: String?) = values().find { it.parseText == text } ?: UNKNOWN
    }
}

enum class VendorColor(
    @get:ColorRes @ColorRes val colorId: Int
) {
    RED(R.color.vendor_red),
    ORANGE(R.color.vendor_orange),
    YELLOW(R.color.vendor_yellow),
    GREEN(R.color.vendor_green),
    BLUE(R.color.vendor_blue),
    PURPLE(R.color.vendor_purple),
    UNKNOWN(R.color.light_grey);

    companion object {
        fun fromColorString(color: String?): VendorColor =
                values().find { it.name.toLowerCase(Locale.ROOT) == color?.toLowerCase(Locale.ROOT) } ?: UNKNOWN
    }
}
