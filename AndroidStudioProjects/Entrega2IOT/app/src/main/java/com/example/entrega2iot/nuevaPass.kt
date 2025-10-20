package com.example.entrega2iot

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import java.util.regex.Pattern

class nuevaPass : AppCompatActivity() {

    private lateinit var nuevaClave: EditText
    private lateinit var nuevaRepClave: EditText
    private lateinit var btnGuardarNuevaPass: Button
    private lateinit var btnSalirNuevaPass: Button // Se añade el nuevo botón
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nueva_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nuevaClave = findViewById(R.id.nuevaClave)
        nuevaRepClave = findViewById(R.id.nuevaRepClave)
        btnGuardarNuevaPass = findViewById(R.id.btnGuardarNuevaPass)
        btnSalirNuevaPass = findViewById(R.id.btnSalirNuevaPass) // Se enlaza el nuevo botón

        userEmail = intent.getStringExtra("USER_EMAIL")

        if (userEmail == null) {
            Toast.makeText(this, "Error: No se pudo obtener el email del usuario.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        btnGuardarNuevaPass.setOnClickListener {
            guardarNuevaContraseña()
        }

        // Se añade la lógica para el botón Salir
        btnSalirNuevaPass.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Estás seguro que quieres salir?")
                .setContentText("Tendrás que solicitar otro código.")
                .setConfirmText("Sí, salir")
                .setCancelText("No, cancelar")
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                    val intent = Intent(this, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setCancelClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                }
                .show()
        }
    }

    private fun guardarNuevaContraseña() {
        val clave = nuevaClave.text.toString().trim()
        val claveRep = nuevaRepClave.text.toString().trim()

        if (!validarClave(clave)) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Contraseña Débil").setContentText("La contraseña debe tener al menos 8 caracteres, 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial.").show()
            return
        }

        if (clave != claveRep) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Contraseñas no coinciden").setContentText("Las contraseñas ingresadas no son iguales.").show()
            return
        }

        val helper = ConexionDbHelper(this)
        val db = helper.writableDatabase

        try {
            val values = ContentValues().apply {
                put("Clave", clave)
            }
            val rowsAffected = db.update("USUARIOS", values, "EMAIL = ?", arrayOf(userEmail))

            if (rowsAffected > 0) {
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("¡Éxito!")
                    .setContentText("Tu contraseña ha sido actualizada.")
                    .setConfirmClickListener {
                        val intent = Intent(this, Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .show()
            } else {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error").setContentText("No se pudo actualizar la contraseña.").show()
            }
        } catch (e: Exception) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Error de Servidor").setContentText(e.message).show()
        } finally {
            db.close()
        }
    }

    private fun validarClave(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^" +
            "(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*[@#$%^&+=!?.])" +
            "(?=\\S+$)" +
            ".{8,}" +
            "$"
        )
        return passwordPattern.matcher(password).matches()
    }
}