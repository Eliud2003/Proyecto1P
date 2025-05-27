package com.example.proyecto1p.compras

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.Farmaceuticos.FarmaceuticoDatabaseHelper
import com.example.proyecto1p.Farmaceuticos.Farmaceuticos
import com.example.proyecto1p.R
import com.example.proyecto1p.medicinas.MedicinaDatabaseHelper
import com.example.proyecto1p.medicinas.Medicinas
import java.text.SimpleDateFormat
import java.util.*

class NuevaVentaActivity : AppCompatActivity() {

    private lateinit var comprasDb: ComprasDatabaseHelper
    private lateinit var farmaceuticosDb: FarmaceuticoDatabaseHelper
    private lateinit var medicinasDb: MedicinaDatabaseHelper

    private lateinit var spinnerClientes: Spinner
    private lateinit var spinnerFarmaceuticos: Spinner
    private lateinit var btnAgregarCliente: Button
    private lateinit var btnAgregarMedicina: Button
    private lateinit var recyclerMedicinas: RecyclerView
    private lateinit var txtSubtotal: TextView
    private lateinit var txtIva: TextView
    private lateinit var txtTotal: TextView
    private lateinit var btnGenerarFactura: Button

    private var selectedClienteId = -1
    private var selectedFarmaceuticoId = -1
    private val medicinasSeleccionadas = mutableListOf<MedicinaSeleccionada>()
    private lateinit var medicinasAdapter: MedicinasSeleccionadasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_venta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        initializeDatabases()
        setupSpinners()
        setupRecyclerView()
        setupButtons()
    }

    private fun initializeViews() {
        spinnerClientes = findViewById(R.id.spinnerClientes)
        spinnerFarmaceuticos = findViewById(R.id.spinnerFarmaceuticos)
        btnAgregarCliente = findViewById(R.id.btnAgregarCliente)
        btnAgregarMedicina = findViewById(R.id.btnAgregarMedicina)
        recyclerMedicinas = findViewById(R.id.recyclerMedicinas)
        txtSubtotal = findViewById(R.id.txtSubtotal)
        txtIva = findViewById(R.id.txtIva)
        txtTotal = findViewById(R.id.txtTotal)
        btnGenerarFactura = findViewById(R.id.btnGenerarFactura)
    }

    private fun initializeDatabases() {
        comprasDb = ComprasDatabaseHelper(this)
        farmaceuticosDb = FarmaceuticoDatabaseHelper(this)
        medicinasDb = MedicinaDatabaseHelper(this)
    }

    private fun setupSpinners() {
        // Configurar spinner de clientes
        val clientes = comprasDb.getAllClientes()
        val clientesNames = listOf("Seleccionar cliente...") + clientes.map { "${it.nombreCompleto} - ${it.cedula}" }
        val clientesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientesNames)
        clientesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClientes.adapter = clientesAdapter

        spinnerClientes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedClienteId = if (position > 0) clientes[position - 1].id else -1
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Configurar spinner de farmacéuticos
        val farmaceuticos = farmaceuticosDb.getAllFarmaceuticos()
        val farmaceuticosNames = listOf("Seleccionar farmacéutico...") + farmaceuticos.map { it.nombreCompleto }
        val farmaceuticosAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, farmaceuticosNames)
        farmaceuticosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFarmaceuticos.adapter = farmaceuticosAdapter

        spinnerFarmaceuticos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedFarmaceuticoId = if (position > 0) farmaceuticos[position - 1].id else -1
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
        medicinasAdapter = MedicinasSeleccionadasAdapter(medicinasSeleccionadas) { medicina ->
            eliminarMedicina(medicina)
        }
        recyclerMedicinas.layoutManager = LinearLayoutManager(this)
        recyclerMedicinas.adapter = medicinasAdapter
    }

    private fun setupButtons() {
        btnAgregarCliente.setOnClickListener {
            val intent = Intent(this, AddClienteActivity::class.java)
            startActivityForResult(intent, 100)
        }

        btnAgregarMedicina.setOnClickListener {
            mostrarDialogoSeleccionMedicina()
        }

        btnGenerarFactura.setOnClickListener {
            generarFactura()
        }
    }

    private fun mostrarDialogoSeleccionMedicina() {
        val medicinas = medicinasDb.getAllMedicinas()
        val medicinasNames = medicinas.map { "${it.nombre} - $${it.precio}" }.toTypedArray()

        var selectedIndex = -1
        AlertDialog.Builder(this)
            .setTitle("Seleccionar Medicina")
            .setSingleChoiceItems(medicinasNames, -1) { _, which ->
                selectedIndex = which
            }
            .setPositiveButton("Seleccionar") { _, _ ->
                if (selectedIndex >= 0) {
                    mostrarDialogoCantidad(medicinas[selectedIndex])
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoCantidad(medicina: Medicinas) {
        val editText = EditText(this)
        editText.hint = "Cantidad"
        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Cantidad de ${medicina.nombre}")
            .setView(editText)
            .setPositiveButton("Agregar") { _, _ ->
                val cantidad = editText.text.toString().toIntOrNull()
                if (cantidad != null && cantidad > 0) {
                    val stockDisponible = medicina.stock.toIntOrNull() ?: 0
                    if (cantidad <= stockDisponible) {
                        agregarMedicina(medicina, cantidad)
                    } else {
                        Toast.makeText(this, "Cantidad excede el stock disponible ($stockDisponible)", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun agregarMedicina(medicina: Medicinas, cantidad: Int) {
        val precio = medicina.precio.toDoubleOrNull() ?: 0.0
        val subtotal = precio * cantidad

        val medicinaSeleccionada = MedicinaSeleccionada(
            id = medicina.id,
            nombre = medicina.nombre,
            precio = precio,
            cantidad = cantidad,
            subtotal = subtotal
        )

        medicinasSeleccionadas.add(medicinaSeleccionada)
        medicinasAdapter.notifyDataSetChanged()
        calcularTotales()
    }

    private fun eliminarMedicina(medicina: MedicinaSeleccionada) {
        medicinasSeleccionadas.remove(medicina)
        medicinasAdapter.notifyDataSetChanged()
        calcularTotales()
    }

    private fun calcularTotales() {
        val subtotal = medicinasSeleccionadas.sumOf { it.subtotal }
        val iva = subtotal * 0.12 // 12% IVA
        val total = subtotal + iva

        txtSubtotal.text = "Subtotal: $%.2f".format(subtotal)
        txtIva.text = "IVA (12%%): $%.2f".format(iva)
        txtTotal.text = "Total: $%.2f".format(total)
    }

    private fun generarFactura() {
        if (selectedClienteId == -1) {
            Toast.makeText(this, "Seleccione un cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedFarmaceuticoId == -1) {
            Toast.makeText(this, "Seleccione un farmacéutico", Toast.LENGTH_SHORT).show()
            return
        }

        if (medicinasSeleccionadas.isEmpty()) {
            Toast.makeText(this, "Agregue al menos una medicina", Toast.LENGTH_SHORT).show()
            return
        }

        val subtotal = medicinasSeleccionadas.sumOf { it.subtotal }
        val iva = subtotal * 0.12
        val total = subtotal + iva

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val factura = Factura(
            id = 0,
            clienteId = selectedClienteId,
            farmaceuticoId = selectedFarmaceuticoId,
            fecha = fecha,
            subtotal = subtotal,
            iva = iva,
            total = total
        )

        val facturaId = comprasDb.insertFactura(factura)

        // Insertar detalles de la factura
        for (medicina in medicinasSeleccionadas) {
            val detalle = DetalleFactura(
                id = 0,
                facturaId = facturaId.toInt(),
                medicinaId = medicina.id,
                cantidad = medicina.cantidad,
                precioUnitario = medicina.precio,
                subtotal = medicina.subtotal
            )
            comprasDb.insertDetalleFactura(detalle)
        }

        Toast.makeText(this, "Factura generada exitosamente", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setupSpinners() // Recargar clientes
        }
    }
}

data class MedicinaSeleccionada(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val subtotal: Double
)