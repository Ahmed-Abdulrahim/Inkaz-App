package com.elkfrawy.engaz.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Problem(
    val title: String?="",
    val description: String?="",
    val latitude: String?="",
    val longitude: String?="",
    val problem_type: String?="",
    val date: String?="",
):Parcelable
