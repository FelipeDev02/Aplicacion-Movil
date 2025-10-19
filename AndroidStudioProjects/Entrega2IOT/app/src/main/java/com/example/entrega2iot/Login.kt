package com.example.entrega2iot

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

lateinit var btningresar: Button
lateinit var registro: TextView
lateinit var olvide: TextView

private lateinit var fechalogin: TextView

val lHandler = Handler(Looper.getMainLooper())


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btningresar = findViewById( R.id.btnlogin)
        registro = findViewById(R.id.registro)
        olvide = findViewById(R.id.olvide)
        fechalogin = findViewById(R.id.txt_fechalogin)
        lHandler.post(refrescarlogin)

        btningresar.setOnClickListener {
            val intent = Intent(this, principal::class.java)
            startActivity(intent)
        }

        registro.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }

        olvide.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }
        //fechalogin.text = fechahora()
    }

    fun fechahora(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf: SimpleDateFormat = SimpleDateFormat("dd MMMM YYYY, hh:mm:ss a")
        val strDate: String = sdf.format(c.getTime())
        return strDate
    }

    private val refrescarlogin = object : Runnable {
        override fun run() {
            fechalogin?.text = fechahora()
            lHandler.postDelayed(this, 1000)
        }
    }


}