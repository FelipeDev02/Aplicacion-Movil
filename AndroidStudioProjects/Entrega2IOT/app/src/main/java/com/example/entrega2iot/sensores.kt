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

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import cn.pedant.SweetAlert.SweetAlertDialog

lateinit var fecha: TextView
lateinit var temp: android.widget.TextView
lateinit var hum: android.widget.TextView
lateinit var imagenTemp: ImageView
lateinit var datos: RequestQueue

lateinit var ampolleta: ImageView
lateinit var linterna: ImageView
// Se añade la variable para el botón Volver
lateinit var btnVolver: Button

val mHandler = Handler(Looper.getMainLooper())

class sensores : AppCompatActivity() {

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

        fecha=findViewById(R.id.txt_fechalogin)
        temp=findViewById(R.id.txt_temp)
        hum=findViewById(R.id.txt_humedad)
        imagenTemp=findViewById(R.id.imagen_temp)
        ampolleta = findViewById(R.id.img_ampolleta)
        linterna = findViewById(R.id.img_linterna)
        // Se inicializa el botón
        btnVolver = findViewById(R.id.btnVolverPrincipalSensores)

        datos = Volley.newRequestQueue(this)
        mHandler.post(refrescar)

        ampolleta.setOnClickListener { alternarAmpolleta() }
        linterna.setOnClickListener { alternarLinterna() }

        // Se añade el listener para el botón Volver
        btnVolver.setOnClickListener {
            val intent = Intent(this, principal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun fechahora(): String {
        val c: Calendar = Calendar.getInstance()
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
            mHandler.postDelayed(this, 1000)
        }
    }

    private fun alternarAmpolleta() {
        ampolletaEncendida = !ampolletaEncendida
        val mensaje = if (ampolletaEncendida) "Ampolleta encendida" else "Ampolleta apagada"

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
            val cameraId = cameraManager.cameraIdList[0]
            linternaEncendida = !linternaEncendida
            cameraManager.setTorchMode(cameraId, linternaEncendida)

            val icono = if (linternaEncendida) R.drawable.linterna_on else R.drawable.linterna_off
            linterna.setImageResource(icono)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}