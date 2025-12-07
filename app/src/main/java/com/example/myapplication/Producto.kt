package com.example.myapplication

data class Producto(
    val codigo_barra: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val categoria: String = "",
    val url: String = ""
)