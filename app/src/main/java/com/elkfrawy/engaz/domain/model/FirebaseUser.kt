package com.elkfrawy.engaz.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FirebaseUser(
    var name: String?= "",
    var address: String? = "",
    var nationalId: String? = "",
    var mobile: String? = "",
    var longitude: String? = "",
    val latitude: String? = "",
    val rating: Int? = 0,
    val url: String ?= "",
    val total_rate:Float? = 0.0f,
    val total_rater: Int? = 0,

):Parcelable