package com.example.proyecto1p

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.AlertDialog
import android.widget.ImageButton
import com.example.proyecto1p.Farmaceuticos.InicioFarmaceuticosActivity
import com.example.proyecto1p.medicinas.InicioMedicinasActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var cardFarmaceuticos: ImageButton
    private lateinit var cardMedicinas: ImageButton
    private lateinit var cardCompra: ImageButton
    private lateinit var usernameTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cardFarmaceuticos = findViewById(R.id.cardFarmaceuticos)
        cardMedicinas = findViewById(R.id.cardMedicinas)
        cardCompra = findViewById(R.id.cardCompra)
        usernameTextView = findViewById(R.id.usernameTextView)

        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        nombreUsuario?.let {
            usernameTextView.text = "Bienvenido, $it"
        }

        cardFarmaceuticos.setOnClickListener {
            irFarmaceuticos()
        }

        cardMedicinas.setOnClickListener {
            irMedicinas()
        }

        cardCompra.setOnClickListener {
            irCompras()
        }
    }

    fun abrirAcercaDe(v: View?) {
        val inflater = LayoutInflater.from(this)
        val modalView = inflater.inflate(R.layout.activity_acerca_de, null)

        val dialog = AlertDialog.Builder(this)
            .setView(modalView)
            .create()

        val btnRegresar = modalView.findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun cerrarSesion(v: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun irFarmaceuticos() {
        val intent = Intent(this, InicioFarmaceuticosActivity::class.java)
        startActivity(intent)
    }

    private fun irMedicinas() {
        val intent = Intent(this, InicioMedicinasActivity::class.java)
        startActivity(intent)
    }

    private fun irCompras() {

    }
}