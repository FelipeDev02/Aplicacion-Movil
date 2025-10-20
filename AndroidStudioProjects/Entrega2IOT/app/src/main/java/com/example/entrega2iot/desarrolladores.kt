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

        // Configurar el listener para el primer desarrollador
        linkGithubFelipe.setOnClickListener {
            // Reemplaza con la URL real del perfil de GitHub de Nicolas
            val url = "https://github.com/FelipeDev02"
            abrirUrl(url)
        }

        // Configurar el listener para el segundo desarrollador
        linkGithubDito.setOnClickListener {
            // Reemplaza con la URL real del perfil de GitHub de Alex
            val url = "https://github.com/Ditoplz"
            abrirUrl(url)
        }
    }

    private fun abrirUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}