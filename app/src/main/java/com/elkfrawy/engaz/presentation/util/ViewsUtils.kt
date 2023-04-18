package com.elkfrawy.engaz.presentation.util


import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun showSnackBar(view: View, message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, length).setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK).show()
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun View.enable(){
    isEnabled = true
}

fun View.disable(){
    isEnabled = false
}



