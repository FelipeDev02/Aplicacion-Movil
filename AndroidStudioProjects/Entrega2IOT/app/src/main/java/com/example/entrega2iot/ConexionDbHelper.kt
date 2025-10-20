package com.example.entrega2iot

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionDbHelper(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Se añade la restricción UNIQUE a la columna EMAIL
    val sql = "CREATE TABLE USUARIOS (ID INTEGER PRIMARY KEY, NOMBRE TEXT, APELLIDO TEXT, EMAIL TEXT UNIQUE, CLAVE TEXT, ESTADO INTEGER DEFAULT 1, CODIGO_RECUPERACION TEXT, EXPIRACION_CODIGO INTEGER)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE USUARIOS ADD COLUMN ESTADO INTEGER DEFAULT 1")
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE USUARIOS ADD COLUMN CODIGO_RECUPERACION TEXT")
            db.execSQL("ALTER TABLE USUARIOS ADD COLUMN EXPIRACION_CODIGO INTEGER")
        }
        // Se añade el bloque para la versión 4, que crea un índice único para los usuarios existentes
        if (oldVersion < 4) {
            db.execSQL("CREATE UNIQUE INDEX idx_email_unique ON USUARIOS (EMAIL)")
        }
    }

    companion object {
        private const val DATABASE_NAME = "CRUD"
        // Se incrementa la versión de la base de datos a 4
        private const val DATABASE_VERSION = 4
    }
}