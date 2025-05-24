package com.example.proyecto1p.Farmaceuticos

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto1p.R
import com.example.proyecto1p.databinding.ActivityInicioFarmaceuticosBinding


class InicioFarmaceuticosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioFarmaceuticosBinding
    private lateinit var db: FarmaceuticoDatabaseHelper
    private lateinit var farmaceuticoAdapter: FarmaceuticosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioFarmaceuticosBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FarmaceuticoDatabaseHelper(this)
        farmaceuticoAdapter = FarmaceuticosAdapter(db.getAllFarmaceuticos(),this)

        binding.farmaceuticosRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.farmaceuticosRecyclerView.adapter = farmaceuticoAdapter

        binding.editBuscarFarmaceutico.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                farmaceuticoAdapter.filterByName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        binding.addButton.setOnClickListener {
            val intent = Intent(this, addFarmaceuticosActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume(){
        super.onResume()
        val allFarmaceuticos = db.getAllFarmaceuticos()
        farmaceuticoAdapter.refreshData(allFarmaceuticos)
    }
}