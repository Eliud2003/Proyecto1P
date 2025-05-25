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
import com.example.proyecto1p.databinding.ActivityUpdateMedicinaBinding

class UpdateMedicinaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateMedicinaBinding
    private lateinit var db: MedicinaDatabaseHelper
    private var medicinaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateMedicinaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }//t

        db = MedicinaDatabaseHelper(this)
        medicinaId = intent.getIntExtra("medicina_id", -1)
        if(medicinaId == -1){
            finish()
            return
        }

        val medicina = db.getMedicinaById(medicinaId)
        binding.updateNombre.setText(medicina.nombre)
        binding.updatePrincipioActivo.setText(medicina.principioActivo)
        binding.updateConcentracion.setText(medicina.concentracion)
        binding.updatePrecio.setText(medicina.precio)
        binding.updateStock.setText(medicina.stock)
        binding.updateFechaVencimiento.setText(medicina.fechaVencimiento)

        // Configurar formato automático de fecha
        setupDateFormatter(binding.updateFechaVencimiento)

        binding.btnActualizar.setOnClickListener{
            val newNombre = binding.updateNombre.text.toString()
            val newPrincipioActivo = binding.updatePrincipioActivo.text.toString()
            val newConcentracion = binding.updateConcentracion.text.toString()
            val newPrecio = binding.updatePrecio.text.toString()
            val newStock = binding.updateStock.text.toString()
            val newFechaVencimiento = binding.updateFechaVencimiento.text.toString()

            if (validarCampos(newNombre, newPrincipioActivo, newConcentracion, newPrecio, newStock, newFechaVencimiento)) {
                val updateMedicina = Medicinas(medicinaId, newNombre, newPrincipioActivo,
                    newConcentracion, newPrecio, newStock, newFechaVencimiento)
                db.updateMedicina(updateMedicina)
                finish()
                Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show()
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