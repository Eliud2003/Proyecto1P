package com.example.proyecto1p.compras

data class Factura(
    val id: Int,
    val clienteId: Int,
    val farmaceuticoId: Int,
    val fecha: String,
    val subtotal: Double,
    val iva: Double,
    val total: Double
)

data class DetalleFactura(
    val id: Int,
    val facturaId: Int,
    val medicinaId: Int,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
)