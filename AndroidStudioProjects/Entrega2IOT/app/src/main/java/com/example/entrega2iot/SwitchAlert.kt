package com.example.entrega2iot

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var btn1: Button
private lateinit var btn2: Button
private lateinit var btn3: Button
private lateinit var btn4: Button
private lateinit var btn5: Button
private lateinit var btn6: Button

class SwitchAlert : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_switch_alert)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)

        btn1.setOnClickListener {
            mostrarAdvertencia()
        }

        btn2.setOnClickListener{
            mostrarExito()
        }

        btn3.setOnClickListener{
            mostrarError()
        }

        btn4.setOnClickListener{
            mostrarCargando()
        }

        btn5.setOnClickListener{
            mostrarConfirmacion()
        }

        btn6.setOnClickListener{
            procesoConCargandoYExito()
        }
        
    }
}