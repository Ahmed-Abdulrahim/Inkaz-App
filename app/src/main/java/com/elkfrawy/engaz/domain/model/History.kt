package com.elkfrawy.engaz.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class History(
    val id: Int,
    val userId: Int,
    val title: String,
    val Description: String,
    val date: String,
    val latitude: String,
    val longitude: String,
    val state: Boolean,
    val problemType: String,
):Parcelable
