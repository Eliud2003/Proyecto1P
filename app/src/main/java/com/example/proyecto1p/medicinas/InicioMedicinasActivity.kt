package com.example.proyecto1p.medicinas

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto1p.R
import com.example.proyecto1p.databinding.ActivityInicioMedicinasBinding

class InicioMedicinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioMedicinasBinding
    private lateinit var db: MedicinaDatabaseHelper
    private lateinit var medicinasAdapter: MedicinasAdapter //t

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioMedicinasBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = MedicinaDatabaseHelper(this)
        medicinasAdapter = MedicinasAdapter(db.getAllMedicinas(), this)

        binding.medicinasRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.medicinasRecyclerView.adapter = medicinasAdapter

        binding.editBuscarMedicina.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                medicinasAdapter.filterByName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        binding.addButton.setOnClickListener {
            val intent = Intent(this, addMedicinasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume(){
        super.onResume()
        val allMedicinas = db.getAllMedicinas()
        medicinasAdapter.refreshData(allMedicinas)
    }
}