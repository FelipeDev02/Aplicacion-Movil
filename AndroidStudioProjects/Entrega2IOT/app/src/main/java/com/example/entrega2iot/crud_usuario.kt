package com.example.entrega2iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var btnregistrar: Button
private lateinit var btnlistar: Button
private lateinit var btnVolverPrincipal: Button

class crud_usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.crud_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnregistrar = findViewById(R.id.btnregistrar)
        btnlistar = findViewById(R.id.btnlistar)
        // 1. Se enlaza el nuevo botón
        btnVolverPrincipal = findViewById(R.id.btnVolverPrincipal)

        btnregistrar.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }

        btnlistar.setOnClickListener {
            val intent = Intent(this, Listar::class.java)
            startActivity(intent)
        }

        // 2. Se añade la lógica para volver a la pantalla principal
        btnVolverPrincipal.setOnClickListener {
            val intent = Intent(this, principal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}