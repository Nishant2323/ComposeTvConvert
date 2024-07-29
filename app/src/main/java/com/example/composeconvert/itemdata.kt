package com.example.mytv11

data class itemdata(
    var title: String,
    var image: Int,
    var isRecent: Boolean = false,
    var isFavorite: Boolean = false
)
