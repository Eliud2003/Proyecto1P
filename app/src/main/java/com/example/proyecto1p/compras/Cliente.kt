package com.example.proyecto1p.compras

data class Cliente(
    val id: Int,
    val nombreCompleto: String,
    val cedula: String,
    val telefono: String,
    val direccion: String,
    val email: String
)