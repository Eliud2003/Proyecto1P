package com.example.proyecto1p.compras

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ComprasDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "compras.db"
        private const val DATABASE_VERSION = 1

        // Tabla Clientes
        private const val TABLE_CLIENTES = "clientes"
        private const val COLUMN_CLIENTE_ID = "id"
        private const val COLUMN_CLIENTE_NOMBRE = "nombreCompleto"
        private const val COLUMN_CLIENTE_CEDULA = "cedula"
        private const val COLUMN_CLIENTE_TELEFONO = "telefono"
        private const val COLUMN_CLIENTE_DIRECCION = "direccion"
        private const val COLUMN_CLIENTE_EMAIL = "email"

        // Tabla Facturas
        private const val TABLE_FACTURAS = "facturas"
        private const val COLUMN_FACTURA_ID = "id"
        private const val COLUMN_FACTURA_CLIENTE_ID = "clienteId"
        private const val COLUMN_FACTURA_FARMACEUTICO_ID = "farmaceuticoId"
        private const val COLUMN_FACTURA_FECHA = "fecha"
        private const val COLUMN_FACTURA_SUBTOTAL = "subtotal"
        private const val COLUMN_FACTURA_IVA = "iva"
        private const val COLUMN_FACTURA_TOTAL = "total"

        // Tabla Detalle Facturas
        private const val TABLE_DETALLE_FACTURAS = "detalle_facturas"
        private const val COLUMN_DETALLE_ID = "id"
        private const val COLUMN_DETALLE_FACTURA_ID = "facturaId"
        private const val COLUMN_DETALLE_MEDICINA_ID = "medicinaId"
        private const val COLUMN_DETALLE_CANTIDAD = "cantidad"
        private const val COLUMN_DETALLE_PRECIO_UNITARIO = "precioUnitario"
        private const val COLUMN_DETALLE_SUBTOTAL = "subtotal"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla clientes
        val createClientesTable = """
            CREATE TABLE $TABLE_CLIENTES (
                $COLUMN_CLIENTE_ID INTEGER PRIMARY KEY,
                $COLUMN_CLIENTE_NOMBRE TEXT,
                $COLUMN_CLIENTE_CEDULA TEXT UNIQUE,
                $COLUMN_CLIENTE_TELEFONO TEXT,
                $COLUMN_CLIENTE_DIRECCION TEXT,
                $COLUMN_CLIENTE_EMAIL TEXT
            )
        """.trimIndent()

        // Crear tabla facturas
        val createFacturasTable = """
            CREATE TABLE $TABLE_FACTURAS (
                $COLUMN_FACTURA_ID INTEGER PRIMARY KEY,
                $COLUMN_FACTURA_CLIENTE_ID INTEGER,
                $COLUMN_FACTURA_FARMACEUTICO_ID INTEGER,
                $COLUMN_FACTURA_FECHA TEXT,
                $COLUMN_FACTURA_SUBTOTAL REAL,
                $COLUMN_FACTURA_IVA REAL,
                $COLUMN_FACTURA_TOTAL REAL,
                FOREIGN KEY($COLUMN_FACTURA_CLIENTE_ID) REFERENCES $TABLE_CLIENTES($COLUMN_CLIENTE_ID),
                FOREIGN KEY($COLUMN_FACTURA_FARMACEUTICO_ID) REFERENCES farmaceuticos(id)
            )
        """.trimIndent()

        // Crear tabla detalle facturas
        val createDetalleFacturasTable = """
            CREATE TABLE $TABLE_DETALLE_FACTURAS (
                $COLUMN_DETALLE_ID INTEGER PRIMARY KEY,
                $COLUMN_DETALLE_FACTURA_ID INTEGER,
                $COLUMN_DETALLE_MEDICINA_ID INTEGER,
                $COLUMN_DETALLE_CANTIDAD INTEGER,
                $COLUMN_DETALLE_PRECIO_UNITARIO REAL,
                $COLUMN_DETALLE_SUBTOTAL REAL,
                FOREIGN KEY($COLUMN_DETALLE_FACTURA_ID) REFERENCES $TABLE_FACTURAS($COLUMN_FACTURA_ID),
                FOREIGN KEY($COLUMN_DETALLE_MEDICINA_ID) REFERENCES Medicinas(id)
            )
        """.trimIndent()

        db?.execSQL(createClientesTable)
        db?.execSQL(createFacturasTable)
        db?.execSQL(createDetalleFacturasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DETALLE_FACTURAS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FACTURAS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        onCreate(db)
    }

    // CRUD Clientes
    fun insertCliente(cliente: Cliente): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CLIENTE_NOMBRE, cliente.nombreCompleto)
            put(COLUMN_CLIENTE_CEDULA, cliente.cedula)
            put(COLUMN_CLIENTE_TELEFONO, cliente.telefono)
            put(COLUMN_CLIENTE_DIRECCION, cliente.direccion)
            put(COLUMN_CLIENTE_EMAIL, cliente.email)
        }
        val result = db.insert(TABLE_CLIENTES, null, values)
        db.close()
        return result
    }

    fun getAllClientes(): List<Cliente> {
        val clientesList = mutableListOf<Cliente>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENTES"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_NOMBRE))
            val cedula = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_CEDULA))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_TELEFONO))
            val direccion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_DIRECCION))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_EMAIL))

            val cliente = Cliente(id, nombre, cedula, telefono, direccion, email)
            clientesList.add(cliente)
        }
        cursor.close()
        db.close()
        return clientesList
    }

    fun getClienteById(clienteId: Int): Cliente? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENTES WHERE $COLUMN_CLIENTE_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(clienteId.toString()))

        var cliente: Cliente? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_NOMBRE))
            val cedula = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_CEDULA))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_TELEFONO))
            val direccion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_DIRECCION))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE_EMAIL))

            cliente = Cliente(id, nombre, cedula, telefono, direccion, email)
        }
        cursor.close()
        db.close()
        return cliente
    }

    // CRUD Facturas
    fun insertFactura(factura: Factura): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FACTURA_CLIENTE_ID, factura.clienteId)
            put(COLUMN_FACTURA_FARMACEUTICO_ID, factura.farmaceuticoId)
            put(COLUMN_FACTURA_FECHA, factura.fecha)
            put(COLUMN_FACTURA_SUBTOTAL, factura.subtotal)
            put(COLUMN_FACTURA_IVA, factura.iva)
            put(COLUMN_FACTURA_TOTAL, factura.total)
        }
        val result = db.insert(TABLE_FACTURAS, null, values)
        db.close()
        return result
    }

    fun insertDetalleFactura(detalle: DetalleFactura): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DETALLE_FACTURA_ID, detalle.facturaId)
            put(COLUMN_DETALLE_MEDICINA_ID, detalle.medicinaId)
            put(COLUMN_DETALLE_CANTIDAD, detalle.cantidad)
            put(COLUMN_DETALLE_PRECIO_UNITARIO, detalle.precioUnitario)
            put(COLUMN_DETALLE_SUBTOTAL, detalle.subtotal)
        }
        val result = db.insert(TABLE_DETALLE_FACTURAS, null, values)
        db.close()
        return result
    }

    fun getAllFacturas(): List<Map<String, String>> {
        val facturasList = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val query = """
            SELECT f.*, c.nombreCompleto as clienteNombre, fa.nombreCompleto as farmaceuticoNombre
            FROM $TABLE_FACTURAS f
            JOIN $TABLE_CLIENTES c ON f.$COLUMN_FACTURA_CLIENTE_ID = c.$COLUMN_CLIENTE_ID
            JOIN Farmaceuticos fa ON f.$COLUMN_FACTURA_FARMACEUTICO_ID = fa.id
            ORDER BY f.$COLUMN_FACTURA_FECHA DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val facturaMap = mapOf(
                "id" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FACTURA_ID)).toString(),
                "clienteNombre" to cursor.getString(cursor.getColumnIndexOrThrow("clienteNombre")),
                "farmaceuticoNombre" to cursor.getString(cursor.getColumnIndexOrThrow("farmaceuticoNombre")),
                "fecha" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACTURA_FECHA)),
                "total" to cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FACTURA_TOTAL)).toString()
            )
            facturasList.add(facturaMap)
        }
        cursor.close()
        db.close()
        return facturasList
    }

    fun getDetallesByFacturaId(facturaId: Int): List<Map<String, String>> {
        val detallesList = mutableListOf<Map<String, String>>()
        val db = readableDatabase
        val query = """
            SELECT d.*, m.nombre as medicinaNombre
            FROM $TABLE_DETALLE_FACTURAS d
            JOIN Medicinas m ON d.$COLUMN_DETALLE_MEDICINA_ID = m.id
            WHERE d.$COLUMN_DETALLE_FACTURA_ID = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(facturaId.toString()))
        while (cursor.moveToNext()) {
            val detalleMap = mapOf(
                "medicinaNombre" to cursor.getString(cursor.getColumnIndexOrThrow("medicinaNombre")),
                "cantidad" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DETALLE_CANTIDAD)).toString(),
                "precioUnitario" to cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DETALLE_PRECIO_UNITARIO)).toString(),
                "subtotal" to cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DETALLE_SUBTOTAL)).toString()
            )
            detallesList.add(detalleMap)
        }
        cursor.close()
        db.close()
        return detallesList
    }
}