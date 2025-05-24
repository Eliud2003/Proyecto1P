package com.example.proyecto1p.Farmaceuticos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto1p.R
import com.example.proyecto1p.databinding.ActivityAddFarmaceuticosBinding
import com.example.proyecto1p.databinding.ActivityInicioFarmaceuticosBinding

class addFarmaceuticosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFarmaceuticosBinding
    private lateinit var db: FarmaceuticoDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFarmaceuticosBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FarmaceuticoDatabaseHelper(this)

        binding.btnGuardar.setOnClickListener {
            val nombreCompleto = binding.NombreCompleto.text.toString()
            val cedula = binding.cedula.text.toString()
            val telefono = binding.telefono.text.toString()
            val edad = binding.edad.text.toString()
            val añosExperiencia = binding.experiencia.text.toString()
            val farmaceuticos = Farmaceuticos (0, nombreCompleto, cedula, telefono, edad, añosExperiencia)
            db. insertFarmaceuticos(farmaceuticos)
            finish()
            Toast.makeText(this, "Farmaceutico guardado", Toast.LENGTH_SHORT).show()
        }

    }
}