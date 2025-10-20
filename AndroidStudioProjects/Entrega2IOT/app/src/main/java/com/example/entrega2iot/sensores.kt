package com.example.entrega2iot

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
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

// --- IMPORTS AÑADIDOS ---
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import cn.pedant.SweetAlert.SweetAlertDialog // Asegúrate de tener esta dependencia en tu build.gradle

// --- VARIABLES GLOBALES ---
lateinit var fecha: TextView
lateinit var temp: android.widget.TextView
lateinit var hum: android.widget.TextView
lateinit var imagenTemp: ImageView
lateinit var datos: RequestQueue

// --- VARIABLES GLOBALES AÑADIDAS ---
lateinit var ampolleta: ImageView
lateinit var linterna: ImageView

val mHandler = Handler(Looper.getMainLooper())

class sensores : AppCompatActivity() {

    // --- ESTADOS AÑADIDOS ---
    private var ampolletaEncendida = false
    private var linternaEncendida = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sensores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias existentes
        fecha=findViewById(R.id.txt_fechalogin)
        temp=findViewById(R.id.txt_temp)
        hum=findViewById(R.id.txt_humedad)
        imagenTemp=findViewById(R.id.imagen_temp)


        // --- REFERENCIAS AÑADIDAS ---
        // (Asegúrate de que estos IDs existan en tu 'activity_sensores.xml')
        ampolleta = findViewById(R.id.img_ampolleta)
        linterna = findViewById(R.id.img_linterna)

        // Lógica existente
        datos = Volley.newRequestQueue(this)
        mHandler.post(refrescar)

        // --- LISTENERS AÑADIDOS ---
        ampolleta.setOnClickListener { alternarAmpolleta() }
        linterna.setOnClickListener { alternarLinterna() }
    }

    fun fechahora(): String {
        val c: Calendar = Calendar.getInstance()
        // OJO: El formato 'YYYY' (Week year) puede dar problemas a fin de año.
        // Es más seguro usar 'yyyy' (Calendar year).
        val sdf: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm:ss a")
        val strDate: String = sdf.format(c.time)
        return strDate
    }

    private fun obtenerDatos() {
        val url = "https://www.pnk.cl/muestra_datos.php"

        val request = JsonObjectRequest(
            Request.Method.GET,url, null,
            { response: JSONObject ->
                try {
                    // (Tu lógica de temperatura y humedad)
                    val temperatura = response.getString("temperatura")
                    temp?.text = "$temperatura C"
                    hum?.text = "${response.getString("humedad")} %"

                    val valor = temperatura.toFloat()
                    cambiarImagen(valor)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error: VolleyError ->
                error.printStackTrace()
            }
        )
        datos.add(request)
    }

    private fun cambiarImagen(valor: Float) {
        // (Tu lógica de imágenes de temperatura)
        if (valor >= 20) {
            imagenTemp.setImageResource(R.drawable.tempalta)
        } else {
            imagenTemp.setImageResource(R.drawable.tempbaja)
        }
    }

    private val refrescar = object : Runnable {
        override fun run() {
            fecha?.text = fechahora()
            obtenerDatos()
            mHandler.postDelayed(this, 1000) // (Tu delay de 1 segundo)
        }
    }

    // --- FUNCIONES AÑADIDAS ---

    private fun alternarAmpolleta() {
        ampolletaEncendida = !ampolletaEncendida
        val mensaje = if (ampolletaEncendida) "Ampolleta encendida" else "Ampolleta apagada"

        // (Asegúrate de tener estos drawables: 'img_ampolleta' y 'ampolleta_off')
        val icono = if (ampolletaEncendida) R.drawable.img_ampolleta else R.drawable.ampolleta_off

        ampolleta.setImageResource(icono)

        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Estado")
            .setContentText(mensaje)
            .setConfirmText("OK")
            .show()
    }

    private fun alternarLinterna() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0] // Usualmente la cámara trasera
            linternaEncendida = !linternaEncendida
            cameraManager.setTorchMode(cameraId, linternaEncendida)

            // (Asegúrate de tener estos drawables: 'linterna_on' y 'linterna_off')
            val icono = if (linternaEncendida) R.drawable.linterna_on else R.drawable.linterna_off
            linterna.setImageResource(icono)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: Exception) {
            // Captura genérica por si no hay flash o falla cameraId
            e.printStackTrace()
        }
    }
}