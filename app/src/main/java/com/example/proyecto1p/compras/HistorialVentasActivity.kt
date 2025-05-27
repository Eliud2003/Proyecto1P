package com.example.proyecto1p.compras

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HistorialVentasActivity : AppCompatActivity() {

    private lateinit var recyclerFacturas: RecyclerView
    private lateinit var fabNuevaVenta: FloatingActionButton
    private lateinit var db: ComprasDatabaseHelper
    private lateinit var facturasAdapter: FacturasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial_ventas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupRecyclerView()
        setupFab()
        loadFacturas()
    }

    private fun initializeViews() {
        recyclerFacturas = findViewById(R.id.recyclerFacturas)
        fabNuevaVenta = findViewById(R.id.fabNuevaVenta)
        db = ComprasDatabaseHelper(this)
    }

    private fun setupRecyclerView() {
        facturasAdapter = FacturasAdapter(emptyList()) { facturaId ->
            val intent = Intent(this, DetalleFacturaActivity::class.java)
            intent.putExtra("facturaId", facturaId)
            startActivity(intent)
        }
        recyclerFacturas.layoutManager = LinearLayoutManager(this)
        recyclerFacturas.adapter = facturasAdapter
    }

    private fun setupFab() {
        fabNuevaVenta.setOnClickListener {
            val intent = Intent(this, NuevaVentaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFacturas() {
        val facturas = db.getAllFacturas()
        facturasAdapter.updateFacturas(facturas)
    }

    override fun onResume() {
        super.onResume()
        loadFacturas()
    }
}