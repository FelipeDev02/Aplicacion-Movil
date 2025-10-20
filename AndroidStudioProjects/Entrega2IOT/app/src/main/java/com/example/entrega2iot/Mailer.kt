package com.example.entrega2iot

import android.os.AsyncTask
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Mailer : AsyncTask<Void, Void, Boolean>() {

    private val emailSalida = "apppenka@gmail.com"

    private val appPass = "naxxnqhgsazdilwu"

    private lateinit var emailReceptor: String
    private lateinit var asunto: String
    private lateinit var mensaje: String

    fun sendMail(para: String, asnt: String, msg: String) {
        emailReceptor = para
        asunto = asnt
        mensaje = msg
        execute()
    }

    override fun doInBackground(vararg params: Void?): Boolean {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(emailSalida, appPass)
            }
        })

        try {
            MimeMessage(session).apply {
                setFrom(InternetAddress(emailSalida))
                addRecipient(Message.RecipientType.TO, InternetAddress(emailReceptor))
                this.subject = this@Mailer.asunto
                setText(mensaje)
                Transport.send(this)
            }
            return true
        } catch (e: MessagingException) {
            e.printStackTrace()
            return false
        }
    }
}