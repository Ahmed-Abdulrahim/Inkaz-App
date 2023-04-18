package com.elkfrawy.engaz.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCar(
    var car_name: String?= "",
    var car_number: Int? = 0,
    var car_year: Int? = 0,
    var car_letter: String? = "",
    var car_color: String? = "",
):Parcelable
