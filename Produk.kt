package com.example.mahasale

data class Produk(
    val id        : Int    = 0,
    val nama      : String,
    val kategori  : String,
    val harga     : String,
    val kondisi   : String,
    val deskripsi : String = "",
    val kontak    : String = "",
    val penjual   : String = "",
    val rating    : Float  = 0f
)
