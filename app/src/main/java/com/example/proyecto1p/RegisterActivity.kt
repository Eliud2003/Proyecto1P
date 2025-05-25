package com.example.proyecto1p

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: BDHelper
    private lateinit var usuarioEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var confirmarContraseñaEditText: EditText
    private lateinit var registrarButton: MaterialButton
    private lateinit var volverLoginButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = BDHelper(this)
        initComponent()
        initListeners()
    }

    private fun initComponent() {
        usuarioEditText = findViewById(R.id.usuarioRegister)
        contraseñaEditText = findViewById(R.id.contraseñaRegister)
        confirmarContraseñaEditText = findViewById(R.id.confirmarContraseña)
        registrarButton = findViewById(R.id.registrarBtn)
        volverLoginButton = findViewById(R.id.volverLoginBtn)
    }

    private fun initListeners() {
        registrarButton.setOnClickListener {
            val usuario = usuarioEditText.text.toString().trim()
            val contraseña = contraseñaEditText.text.toString().trim()
            val confirmarContraseña = confirmarContraseñaEditText.text.toString().trim()

            if (usuario.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contraseña != confirmarContraseña) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contraseña.length < 4) {
                Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Intentar crear el usuario
            val resultado = dbHelper.createUser(usuario, contraseña)

            when (resultado) {
                BDHelper.UserCreationResult.SUCCESS -> {
                    Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Volver al login
                }
                BDHelper.UserCreationResult.USER_EXISTS -> {
                    Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                }
                BDHelper.UserCreationResult.ERROR -> {
                    Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }

        volverLoginButton.setOnClickListener {
            finish() // Volver al login
        }
    }
}