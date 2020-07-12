package com.softwareforgood.pridefestival.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParadeEvent(
    @PrimaryKey
    override val objectId: String,
    val name: String,
    val details: String? = null,
    val lineupNumber: Int,
    val verified: Boolean = false,
    val favorite: Boolean = false
) : HasParseId
