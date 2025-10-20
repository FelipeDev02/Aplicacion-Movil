package com.example.entrega2iot

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var listado: ListView
private lateinit var listausuario: ArrayList<String>
class Listar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listado= findViewById(R.id.lista);
        CargarLista()
    }
    private fun listaUsuario(): ArrayList<String> {
        val datos = ArrayList<String>()
        val helper = ConexionDbHelper(this)
        val db = helper.readableDatabase
        val sql = "SELECT * FROM USUARIOS"
        val c = db.rawQuery(sql, null)
        if (c.moveToFirst()) {
            do {
                val linea = "${c.getInt(0)} ${c.getString(1)} ${c.getString(2)}"
                datos.add(linea)
            } while (c.moveToNext())
        }
        c.close()
        db.close()
        return datos
    }

    private fun CargarLista() {
        listausuario = listaUsuario()
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            listausuario
        )
        listado.adapter = adapter
    }
}