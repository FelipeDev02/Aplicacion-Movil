package com.example.entrega2iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var txtcrud: TextView
private lateinit var txtsensores: TextView

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

        txtcrud = findViewById( R.id.txtcrudusuarios)
        txtsensores = findViewById( R.id.txtsensores)

        txtcrud.setOnClickListener {
            val intent = Intent(this, crud_usuario::class.java)
            startActivity(intent)
        }

        txtsensores.setOnClickListener {
            val intent = Intent(this, sensores::class.java)
            startActivity(intent)
        }
    }
}