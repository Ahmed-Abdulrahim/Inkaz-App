package com.elkfrawy.engaz.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Long = 0,
    var name: String?,
    var password: String?,
    var email: String? = "",
    var address: String?,
    var NationalId: String?,
    var number: Long?,
    var longtitude: String?,
    val latitude: String?,
    val rating: Int?,


):Parcelable