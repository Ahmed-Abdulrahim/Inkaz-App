package com.elkfrawy.engaz.presentation.util

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.domain.model.UserCluster
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class ClusterRender(context: Context,
                    map: GoogleMap, clusterManager: ClusterManager<UserCluster>
) : DefaultClusterRenderer<UserCluster>(context, map, clusterManager){

    var iconGenerator: IconGenerator
    var image: ImageView
    init {

        iconGenerator = IconGenerator(context)
        image = ImageView(context)
        val width =  context.resources.getDimension(R.dimen.custom_marker_image)
        val height = context.resources.getDimension(R.dimen.custom_marker_image)
        image.layoutParams = ViewGroup.LayoutParams(width.toInt(), height.toInt())
        val padding = context.resources.getDimension(R.dimen.custom_marker_padding)
        image.setPadding(padding.toInt(), padding.toInt(), padding.toInt(), padding.toInt())
        iconGenerator.setContentView(image)

    }

    override fun onBeforeClusterItemRendered(item: UserCluster, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)

        image.setImageResource(item.img)
        val bitMap = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitMap)).title(item.name)

    }

    override fun shouldRenderAsCluster(cluster: Cluster<UserCluster>): Boolean {
        return false
    }



}