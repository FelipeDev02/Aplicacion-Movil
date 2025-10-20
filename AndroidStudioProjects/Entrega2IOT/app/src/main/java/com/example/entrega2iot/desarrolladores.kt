package com.example.entrega2iot

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.widget.Button

class desarrolladores : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_desarrolladores)


        val linkGithubFelipe: TextView = findViewById(R.id.link_github_felipe)
        val linkGithubDito: TextView = findViewById(R.id.link_github_dito)
        // 1. Se enlaza el nuevo botón
        val btnVolver: Button = findViewById(R.id.btnVolverPrincipal) 

        linkGithubFelipe.setOnClickListener {
            val url = "https://github.com/FelipeDev02"
            abrirUrl(url)
        }

        linkGithubDito.setOnClickListener {
            val url = "https://github.com/Ditoplz"
            abrirUrl(url)
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, principal::class.java)
            // Limpia la pila de actividades para que el usuario no pueda volver atrás
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun abrirUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}