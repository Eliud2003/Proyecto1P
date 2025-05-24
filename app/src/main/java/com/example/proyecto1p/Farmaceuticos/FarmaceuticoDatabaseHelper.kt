package com.example.proyecto1p.Farmaceuticos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class FarmaceuticoDatabaseHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "farmaceuticos.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Farmaceuticos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRECOMPLETO = "nombreCompleto"
        private const val COLUMN_CEDULA = "cedula"
        private const val COLUMN_TELEFONO = "telefono"
        private const val COLUMN_EDAD = "edad"
        private const val COLUMN_EXPERIENCIA = "añosExperiencia"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, " +
                                                        "$COLUMN_NOMBRECOMPLETO TEXT, " +
                                                        "$COLUMN_CEDULA TEXT, " +
                                                        "$COLUMN_TELEFONO TEXT, " +
                                                        "$COLUMN_EDAD INTEGER, " +
                                                        "$COLUMN_EXPERIENCIA INTEGER)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertFarmaceuticos(farmaceuticos: Farmaceuticos){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRECOMPLETO, farmaceuticos.nombreCompleto)
            put(COLUMN_CEDULA, farmaceuticos.cedula)
            put(COLUMN_TELEFONO, farmaceuticos.telefono)
            put(COLUMN_EDAD, farmaceuticos.edad)
            put(COLUMN_EXPERIENCIA, farmaceuticos.añosExperiencia)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllFarmaceuticos(): List<Farmaceuticos>{
        val farmaceuticosList = mutableListOf<Farmaceuticos>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRECOMPLETO))
            val cedula = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
            val edad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EDAD))
            val añosExperiencia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCIA))

            val Farmaceutico = Farmaceuticos(id, nombreCompleto, cedula, telefono, edad, añosExperiencia)
            farmaceuticosList.add(Farmaceutico)
        }
        cursor.close()
        db.close()
        return farmaceuticosList
    }

    fun updateFarmaceutico (farmaceutico: Farmaceuticos){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRECOMPLETO, farmaceutico.nombreCompleto)
            put(COLUMN_CEDULA, farmaceutico.cedula)
            put(COLUMN_EDAD, farmaceutico.edad)
            put(COLUMN_TELEFONO, farmaceutico.telefono)
            put(COLUMN_EXPERIENCIA, farmaceutico.añosExperiencia)

        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(farmaceutico.id.toString())
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun deleteFarmaceutico ( farmaceuticoId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(farmaceuticoId.toString())

        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }

    fun getFarmaceuticoById(farmaceuticoId: Int): Farmaceuticos{
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $farmaceuticoId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRECOMPLETO))
        val cedula = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA))
        val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
        val edad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EDAD))
        val añosExperiencia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCIA))

        cursor.close()
        db.close()
        return Farmaceuticos(id,nombreCompleto,cedula,telefono,edad,añosExperiencia)
    }
}
