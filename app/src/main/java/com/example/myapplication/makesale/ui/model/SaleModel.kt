package com.example.myapplication.makesale.ui.model

data class SaleModel(
    val id: Int,
    val title: String,
    var quantity: Int = 1,
    var selectedCheckBox: Boolean = false
)