package com.example.entrega2iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.pedant.SweetAlert.SweetAlertDialog

class principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtcrudusuarios = findViewById<TextView>(R.id.txtcrudusuarios)
        val txtsensores = findViewById<TextView>(R.id.txtsensores)
        val txtdesarrollo = findViewById<TextView>(R.id.txtdesarrollo)
        val btnSalirPrincipal = findViewById<Button>(R.id.btnSalirPrincipal)

        txtcrudusuarios.setOnClickListener {
            val intent = Intent(this, crud_usuario::class.java)
            startActivity(intent)
        }

        txtsensores.setOnClickListener {
            val intent = Intent(this, sensores::class.java)
            startActivity(intent)
        }

        txtdesarrollo.setOnClickListener {
            val intent = Intent(this, desarrolladores::class.java)
            startActivity(intent)
        }

        btnSalirPrincipal.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Estás seguro que deseas cerrar sesión?")
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
}