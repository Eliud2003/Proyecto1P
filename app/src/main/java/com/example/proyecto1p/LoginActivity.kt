
package com.example.proyecto1p

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton


class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: BDHelper
    private lateinit var sharedPreferences: SharedPreferences

    // Variables UI como propiedades de clase
    private lateinit var usuarioEditText: EditText
    private lateinit var contraseñaEditText: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var googleButton: ImageView
    private lateinit var facebookButton: ImageView
    private lateinit var checkBoxMantenerSesion: CheckBox

    companion object {
        const val PREFS_NAME = "LoginPrefs"
        const val KEY_USUARIO = "usuario"
        const val KEY_CONTRASENA = "contraseña"
        const val KEY_MANTENER_SESION = "mantenerSesion"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = BDHelper(this)
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        initComponent()
        initListeners()
        cargarSesionGuardada()
    }

    private fun initComponent() {
        usuarioEditText = findViewById(R.id.usuario)
        contraseñaEditText = findViewById(R.id.contraseña)
        loginButton = findViewById(R.id.loginbtn)
        googleButton = findViewById(R.id.googlebtn)
        facebookButton = findViewById(R.id.facebookbtn)
        checkBoxMantenerSesion = findViewById(R.id.checkBoxMantenerSesion)
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            val user = usuarioEditText.text.toString().trim()
            val pass = contraseñaEditText.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar usuario con SQLite
            val esValido = dbHelper.checkUser(user, pass)

            if (esValido) {
                Toast.makeText(this, "Acceso Concedido", Toast.LENGTH_SHORT).show()
                guardarSesion(user, pass, checkBoxMantenerSesion.isChecked)

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("nombreUsuario", user)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        googleButton.setOnClickListener {
            Toast.makeText(this, "Iniciando sesión con Google...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("nombreUsuario", "Xavier")
            startActivity(intent)
            finish()
        }

        facebookButton.setOnClickListener {
            Toast.makeText(this, "Iniciando sesión con Facebook...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("nombreUsuario", "Carlos")
            startActivity(intent)
            finish()
        }
    }

    private fun guardarSesion(usuario: String, contraseña: String, mantenerSesion: Boolean) {
        val editor = sharedPreferences.edit()
        if (mantenerSesion) {
            editor.putString(KEY_USUARIO, usuario)
            editor.putString(KEY_CONTRASENA, contraseña)
            editor.putBoolean(KEY_MANTENER_SESION, true)
        } else {
            editor.clear()
        }
        editor.apply()
    }

    private fun cargarSesionGuardada() {
        val mantenerSesion = sharedPreferences.getBoolean(KEY_MANTENER_SESION, false)
        if (mantenerSesion) {
            val usuarioGuardado = sharedPreferences.getString(KEY_USUARIO, "")
            val contraseñaGuardada = sharedPreferences.getString(KEY_CONTRASENA, "")
            usuarioEditText.setText(usuarioGuardado)
            contraseñaEditText.setText(contraseñaGuardada)
            checkBoxMantenerSesion.isChecked = true
        }
    }

    // Función para abrir URL en navegador (opcional)
    private fun abrirPaginaWeb(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se pudo abrir el navegador", Toast.LENGTH_SHORT).show()
        }
    }
}