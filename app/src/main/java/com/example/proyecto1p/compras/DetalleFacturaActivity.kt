package com.example.proyecto1p.compras

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.R

class DetalleFacturaActivity : AppCompatActivity() {

    private lateinit var txtInfoFactura: TextView
    private lateinit var recyclerDetalles: RecyclerView
    private lateinit var txtSubtotalDetalle: TextView
    private lateinit var txtIvaDetalle: TextView
    private lateinit var txtTotalDetalle: TextView
    private lateinit var db: ComprasDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_factura)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        loadDetalleFactura()
    }

    private fun initializeViews() {
        txtInfoFactura = findViewById(R.id.txtInfoFactura)
        recyclerDetalles = findViewById(R.id.recyclerDetalles)
        txtSubtotalDetalle = findViewById(R.id.txtSubtotalDetalle)
        txtIvaDetalle = findViewById(R.id.txtIvaDetalle)
        txtTotalDetalle = findViewById(R.id.txtTotalDetalle)
        db = ComprasDatabaseHelper(this)
    }

    private fun loadDetalleFactura() {
        val facturaId = intent.getIntExtra("facturaId", -1)
        if (facturaId == -1) {
            finish()
            return
        }

        // Cargar información general de la factura
        val facturas = db.getAllFacturas()
        val factura = facturas.find { it["id"] == facturaId.toString() }

        factura?.let {
            txtInfoFactura.text = """
                Factura #$facturaId
                Cliente: ${it["clienteNombre"]}
                Farmacéutico: ${it["farmaceuticoNombre"]}
                Fecha: ${it["fecha"]}
            """.trimIndent()

            val total = it["total"]?.toDoubleOrNull() ?: 0.0
            val subtotal = total / 1.12 // Calcular subtotal sin IVA
            val iva = total - subtotal

            txtSubtotalDetalle.text = "Subtotal: $%.2f".format(subtotal)
            txtIvaDetalle.text = "IVA (12%%): $%.2f".format(iva)
            txtTotalDetalle.text = "Total: $%.2f".format(total)
        }

        // Cargar detalles de medicinas
        val detalles = db.getDetallesByFacturaId(facturaId)
        val detallesAdapter = DetallesAdapter(detalles)
        recyclerDetalles.layoutManager = LinearLayoutManager(this)
        recyclerDetalles.adapter = detallesAdapter
    }
}