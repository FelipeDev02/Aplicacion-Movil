package com.example.entrega2iot

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionDbHelper(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val sql = "CREATE TABLE USUARIOS (ID INTEGER PRIMARY KEY, NOMBRE TEXT, APELLIDO TEXT, EMAIL TEXT, CLAVE TEXT, ESTADO INTEGER DEFAULT 1, CODIGO_RECUPERACION TEXT, EXPIRACION_CODIGO INTEGER)"

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
    }

    companion object {
        private const val DATABASE_NAME = "CRUD"
        private const val DATABASE_VERSION = 3
    }
}