package com.elkfrawy.engaz.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.elkfrawy.engaz.R
import com.squareup.picasso.Picasso


fun Context.permissionGranted():Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }else{
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }
}


const val PREFERENCE_BOOLEAN_KEY = "INKAS_DS_BOOLEAN_KEY"
const val PREFERENCE_STRING_KEY = "INKAS_DS_STRING_KEY"
const val PREFERENCE_INTEGER_KEY = "INKAS_DS_INTEGER_KEY"

fun loadImage (imageView: ImageView, url:String?){
    if (url?.length ==  0){
        imageView.setImageResource(R.drawable.user2)
    }else{
        url?.let {
            Picasso.get().load(url).into(imageView)
        } ?: run {
            imageView.setImageResource(R.drawable.user2)
        }
    }

}