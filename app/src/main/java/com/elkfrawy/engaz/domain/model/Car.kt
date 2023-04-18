package com.elkfrawy.engaz.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Car(
    var id: Long = 0,
    var car_name: String?,
    var car_number: Int?,
    var car_model: Int?,
    var car_license: String?,
    var car_color: String?,
    var user_id: Long?,
):Parcelable
