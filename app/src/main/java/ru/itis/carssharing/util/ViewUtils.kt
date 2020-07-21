package ru.itis.carssharing.util

import android.view.View

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible() : Boolean {
    return visibility == View.VISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setLoadingState(isLoading: Boolean) {
    visibility = if (isLoading) View.VISIBLE else View.GONE
}