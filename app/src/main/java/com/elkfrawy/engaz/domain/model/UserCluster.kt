package com.elkfrawy.engaz.domain.model

import android.icu.text.CaseMap.Title
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

 data class UserCluster(
      val pos: LatLng,
      val name: String,
      val shortDesc: String,
      val img: Int,
      val user: User?,
):ClusterItem  {


    override fun getPosition(): LatLng {
        return this.pos
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String {
        return shortDesc
    }


}