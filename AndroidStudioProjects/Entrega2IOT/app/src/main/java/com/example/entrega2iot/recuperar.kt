package com.example.entrega2iot

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.pedant.SweetAlert.SweetAlertDialog

lateinit var btn_recuperar : Button
lateinit var emailRecuperar: EditText
lateinit var codigo1: EditText
lateinit var codigo2: EditText
lateinit var codigo3: EditText
lateinit var codigo4: EditText
lateinit var codigo5: EditText
lateinit var timerRecuperar: TextView

class recuperar : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_recuperar = findViewById(R.id.btnRecuperar)
        emailRecuperar = findViewById(R.id.emailRecuperar)
        codigo1 = findViewById(R.id.codigo1)
        codigo2 = findViewById(R.id.codigo2)
        codigo3 = findViewById(R.id.codigo3)
        codigo4 = findViewById(R.id.codigo4)
        codigo5 = findViewById(R.id.codigo5)
        timerRecuperar = findViewById(R.id.timerRecuperar)

        setupCodigoInput()

        btn_recuperar.setOnClickListener {
            val email = emailRecuperar.text.toString().trim()
            if (email.isEmpty()) {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Campo obligatorio").setContentText("Por favor, ingrese su email.").show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Formato Inválido").setContentText("Por favor, ingrese un email con formato válido.").show()
                return@setOnClickListener
            }

            val helper = ConexionDbHelper(this)
            val db = helper.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM USUARIOS WHERE EMAIL = ?", arrayOf(email))

            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("ESTADO")) == 0) {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Usuario Bloqueado").setContentText("Imposible recuperar contraseña, su usuario ha sido bloqueado por PENKAAAA.").show()
                    cursor.close()
                    db.close()
                    return@setOnClickListener
                }

                val codigo = (10000..99999).random().toString()
                val expiracion = System.currentTimeMillis() + 60000

                val values = ContentValues().apply {
                    put("CODIGO_RECUPERACION", codigo)
                    put("EXPIRACION_CODIGO", expiracion)
                }
                db.update("USUARIOS", values, "EMAIL = ?", arrayOf(email))
                iniciarTemporizador()

                val subject = "Código de Recuperación de Contraseña."
                val message = "Hola,\n\nTu código para recuperar tu contraseña es: $codigo\n\nEste código expirará en 60 segundos."
                Mailer().sendMail(email, subject, message)

                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Correo Enviado").setContentText("Se ha enviado un código de recuperación a su correo electrónico con vigencia de 60 segundos").setConfirmText("Aceptar").show()

            } else {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Email no registrado").setContentText("La dirección de correo no se encuentra registrada.").show()
            }
            cursor.close()
            db.close()
        }
    }

    private fun setupCodigoInput() {
        val editTexts = listOf(codigo1, codigo2, codigo3, codigo4, codigo5)
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    } else if (s?.length == 1 && i == editTexts.size - 1) {
                        validarCodigo()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun validarCodigo() {
        val codigoIngresado = "${codigo1.text}${codigo2.text}${codigo3.text}${codigo4.text}${codigo5.text}"
        val email = emailRecuperar.text.toString().trim()

        if (email.isEmpty()) return

        val helper = ConexionDbHelper(this)
        val db = helper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM USUARIOS WHERE EMAIL = ?", arrayOf(email))

        if (cursor.moveToFirst()) {
            val codigoBD = cursor.getString(cursor.getColumnIndexOrThrow("CODIGO_RECUPERACION"))
            val expiracionBD = cursor.getLong(cursor.getColumnIndexOrThrow("EXPIRACION_CODIGO"))

            if (System.currentTimeMillis() > expiracionBD) {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Código Expirado").setContentText("El código de recuperación ha expirado. Por favor, solicita uno nuevo.").show()
                limpiarCamposCodigo()
            } else if (codigoIngresado == codigoBD) {
                countDownTimer?.cancel()
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("¡Código Correcto!")
                    .setContentText("Ahora puedes establecer una nueva contraseña.")
                    .setConfirmText("Continuar")
                    .setConfirmClickListener {
                        val intent = Intent(this, nuevaPass::class.java)
                        intent.putExtra("USER_EMAIL", email)
                        startActivity(intent)
                        finish()
                    }
                    .show()
            } else {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Código Incorrecto").setContentText("El código ingresado no es válido.").show()
                limpiarCamposCodigo()
            }
        } 
        cursor.close()
        db.close()
    }

    private fun limpiarCamposCodigo() {
        codigo1.text.clear()
        codigo2.text.clear()
        codigo3.text.clear()
        codigo4.text.clear()
        codigo5.text.clear()
        codigo1.requestFocus()
    }

    private fun iniciarTemporizador() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerRecuperar.text = "${millisUntilFinished / 1000} Segundos"
            }
            override fun onFinish() {
                timerRecuperar.text = "Código expirado"
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}