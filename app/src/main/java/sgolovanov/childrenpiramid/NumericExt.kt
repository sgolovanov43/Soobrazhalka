package sgolovanov.childrenpiramid

fun Int?.orZero() : Int =
    this ?: 0

fun Float?.orZero() : Float =
    this ?: 0f
