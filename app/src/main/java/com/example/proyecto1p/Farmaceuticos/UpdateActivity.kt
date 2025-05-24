package com.example.proyecto1p.Farmaceuticos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto1p.R
import com.example.proyecto1p.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: FarmaceuticoDatabaseHelper
    private var farmaceuticoId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FarmaceuticoDatabaseHelper(this)
        farmaceuticoId = intent.getIntExtra("farmaceutico_id", -1)
        if(farmaceuticoId == -1){
            finish()
            return
        }

        val farmaceutico = db.getFarmaceuticoById(farmaceuticoId)
        binding.updateNombreCompleto.setText(farmaceutico.nombreCompleto)
        binding.updatecedula.setText(farmaceutico.cedula)
        binding.updatetelefono.setText(farmaceutico.telefono)
        binding.updateedad.setText(farmaceutico.edad)
        binding.updateaOsExperiencia.setText(farmaceutico.añosExperiencia)

        binding.btnActualizar.setOnClickListener{
            val newNombreCompleto = binding.updateNombreCompleto.text.toString()
            val newcedula = binding.updatecedula.text.toString()
            val newtelefono = binding.updatetelefono.text.toString()
            val newedad = binding.updateedad.text.toString()
            val newaOsExperiencia = binding.updateaOsExperiencia.text.toString()
            val updateFarmaceutico = Farmaceuticos(farmaceuticoId, newNombreCompleto, newcedula,
                                                    newtelefono, newedad, newaOsExperiencia)
            db.updateFarmaceutico(updateFarmaceutico)
            finish()
            Toast.makeText(this, "Cambios guardado con exito", Toast.LENGTH_SHORT).show()
        }
    }
}