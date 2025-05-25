package com.example.proyecto1p.medicinas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MedicinaDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "medicinas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Medicinas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_PRINCIPIO_ACTIVO = "principioActivo"
        private const val COLUMN_CONCENTRACION = "concentracion"
        private const val COLUMN_PRECIO = "precio"
        private const val COLUMN_STOCK = "stock"
        private const val COLUMN_FECHA_VENCIMIENTO = "fechaVencimiento"
    }//t

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NOMBRE TEXT, " +
                "$COLUMN_PRINCIPIO_ACTIVO TEXT, " +
                "$COLUMN_CONCENTRACION TEXT, " +
                "$COLUMN_PRECIO TEXT, " +
                "$COLUMN_STOCK TEXT, " +
                "$COLUMN_FECHA_VENCIMIENTO TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertMedicina(medicina: Medicinas){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, medicina.nombre)
            put(COLUMN_PRINCIPIO_ACTIVO, medicina.principioActivo)
            put(COLUMN_CONCENTRACION, medicina.concentracion)
            put(COLUMN_PRECIO, medicina.precio)
            put(COLUMN_STOCK, medicina.stock)
            put(COLUMN_FECHA_VENCIMIENTO, medicina.fechaVencimiento)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllMedicinas(): List<Medicinas>{
        val medicinasList = mutableListOf<Medicinas>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            val principioActivo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRINCIPIO_ACTIVO))
            val concentracion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONCENTRACION))
            val precio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRECIO))
            val stock = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK))
            val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_VENCIMIENTO))

            val medicina = Medicinas(id, nombre, principioActivo, concentracion, precio, stock, fechaVencimiento)
            medicinasList.add(medicina)
        }
        cursor.close()
        db.close()
        return medicinasList
    }

    fun updateMedicina(medicina: Medicinas){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, medicina.nombre)
            put(COLUMN_PRINCIPIO_ACTIVO, medicina.principioActivo)
            put(COLUMN_CONCENTRACION, medicina.concentracion)
            put(COLUMN_PRECIO, medicina.precio)
            put(COLUMN_STOCK, medicina.stock)
            put(COLUMN_FECHA_VENCIMIENTO, medicina.fechaVencimiento)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(medicina.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteMedicina(medicinaId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(medicinaId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getMedicinaById(medicinaId: Int): Medicinas{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $medicinaId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
        val principioActivo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRINCIPIO_ACTIVO))
        val concentracion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONCENTRACION))
        val precio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRECIO))
        val stock = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STOCK))
        val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_VENCIMIENTO))

        cursor.close()
        db.close()
        return Medicinas(id, nombre, principioActivo, concentracion, precio, stock, fechaVencimiento)
    }
}