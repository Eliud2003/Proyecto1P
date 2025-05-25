package com.example.proyecto1p.medicinas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto1p.R
import com.example.proyecto1p.databinding.ActivityAddMedicinasBinding

class addMedicinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicinasBinding
    private lateinit var db: MedicinaDatabaseHelper //t

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicinasBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = MedicinaDatabaseHelper(this)

        // Configurar formato automático de fecha
        setupDateFormatter(binding.fechaVencimiento)

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.nombre.text.toString()
            val principioActivo = binding.principioActivo.text.toString()
            val concentracion = binding.concentracion.text.toString()
            val precio = binding.precio.text.toString()
            val stock = binding.stock.text.toString()
            val fechaVencimiento = binding.fechaVencimiento.text.toString()

            if (validarCampos(nombre, principioActivo, concentracion, precio, stock, fechaVencimiento)) {
                val medicina = Medicinas(0, nombre, principioActivo, concentracion, precio, stock, fechaVencimiento)
                db.insertMedicina(medicina)
                finish()
                Toast.makeText(this, "Medicina guardada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDateFormatter(editText: com.google.android.material.textfield.TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var deletingBackward = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deletingBackward = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val input = s.toString().replace("/", "")
                val formatted = StringBuilder()

                for (i in input.indices) {
                    if (i == 2 || i == 4) {
                        formatted.append("/")
                    }
                    if (i < 8) { // Limitar a 8 dígitos (DD/MM/AAAA)
                        formatted.append(input[i])
                    }
                }

                editText.setText(formatted.toString())
                editText.setSelection(formatted.length)
                isFormatting = false
            }
        })
    }

    private fun validarCampos(nombre: String, principioActivo: String, concentracion: String,
                              precio: String, stock: String, fechaVencimiento: String): Boolean {
        when {
            nombre.isEmpty() -> {
                Toast.makeText(this, "Por favor ingresa el nombre de la medicina", Toast.LENGTH_SHORT).show()
                return false
            }
            principioActivo.isEmpty() -> {
                Toast.makeText(this, "Por favor ingresa el principio activo", Toast.LENGTH_SHORT).show()
                return false
            }
            concentracion.isEmpty() -> {
                Toast.makeText(this, "Por favor ingresa la concentración", Toast.LENGTH_SHORT).show()
                return false
            }
            precio.isEmpty() -> {
                Toast.makeText(this, "Por favor ingresa el precio", Toast.LENGTH_SHORT).show()
                return false
            }
            stock.isEmpty() -> {
                Toast.makeText(this, "Por favor ingresa el stock", Toast.LENGTH_SHORT).show()
                return false
            }
            fechaVencimiento.isEmpty() -> {
                Toast.makeText(this, "Por favor ingresa la fecha de vencimiento", Toast.LENGTH_SHORT).show()
                return false
            }
            fechaVencimiento.length != 10 -> {
                Toast.makeText(this, "Por favor ingresa una fecha válida (DD/MM/AAAA)", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}