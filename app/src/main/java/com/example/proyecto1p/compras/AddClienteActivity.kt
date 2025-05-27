package com.example.proyecto1p.compras

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto1p.R
import com.example.proyecto1p.databinding.ActivityAddClienteBinding

class AddClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClienteBinding
    private lateinit var db: ComprasDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClienteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = ComprasDatabaseHelper(this)

        binding.btnGuardarCliente.setOnClickListener {
            val nombreCompleto = binding.etNombreCompleto.text.toString().trim()
            val cedula = binding.etCedula.text.toString().trim()
            val telefono = binding.etTelefono.text.toString().trim()
            val direccion = binding.etDireccion.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (validarCampos(nombreCompleto, cedula, telefono, direccion, email)) {
                val cliente = Cliente(0, nombreCompleto, cedula, telefono, direccion, email)
                val result = db.insertCliente(cliente)

                if (result != -1L) {
                    Toast.makeText(this, "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validarCampos(nombre: String, cedula: String, telefono: String, direccion: String, email: String): Boolean {
        when {
            nombre.isEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el nombre completo", Toast.LENGTH_SHORT).show()
                return false
            }
            cedula.isEmpty() -> {
                Toast.makeText(this, "Por favor ingrese la cédula", Toast.LENGTH_SHORT).show()
                return false
            }
            cedula.length != 10 -> {
                Toast.makeText(this, "La cédula debe tener 10 dígitos", Toast.LENGTH_SHORT).show()
                return false
            }
            telefono.isEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el teléfono", Toast.LENGTH_SHORT).show()
                return false
            }
            direccion.isEmpty() -> {
                Toast.makeText(this, "Por favor ingrese la dirección", Toast.LENGTH_SHORT).show()
                return false
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Por favor ingrese el email", Toast.LENGTH_SHORT).show()
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Por favor ingrese un email válido", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}