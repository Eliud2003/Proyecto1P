package com.example.proyecto1p

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BDHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "usuarios.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    enum class UserCreationResult {
        SUCCESS,
        USER_EXISTS,
        ERROR
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USERNAME TEXT PRIMARY KEY,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)

        // Insertar usuario admin por defecto
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, "admin")
            put(COLUMN_PASSWORD, "admin")
        }
        db.insert(TABLE_USERS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun createUser(username: String, password: String): UserCreationResult {
        val db = writableDatabase

        return try {
            // Verificar si el usuario ya existe
            val checkQuery = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
            val cursor = db.rawQuery(checkQuery, arrayOf(username))

            if (cursor.count > 0) {
                cursor.close()
                UserCreationResult.USER_EXISTS
            } else {
                cursor.close()

                // Crear el nuevo usuario
                val values = ContentValues().apply {
                    put(COLUMN_USERNAME, username)
                    put(COLUMN_PASSWORD, password)
                }

                val result = db.insert(TABLE_USERS, null, values)
                if (result != -1L) {
                    UserCreationResult.SUCCESS
                } else {
                    UserCreationResult.ERROR
                }
            }
        } catch (e: Exception) {
            UserCreationResult.ERROR
        }
    }
}