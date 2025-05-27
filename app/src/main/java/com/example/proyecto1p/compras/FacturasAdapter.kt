package com.example.proyecto1p.compras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.R

class FacturasAdapter(
    private var facturas: List<Map<String, String>>,
    private val onFacturaClick: (Int) -> Unit
) : RecyclerView.Adapter<FacturasAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNumeroFactura: TextView = itemView.findViewById(R.id.txtNumeroFactura)
        val txtCliente: TextView = itemView.findViewById(R.id.txtClienteFactura)
        val txtFarmaceutico: TextView = itemView.findViewById(R.id.txtFarmaceuticoFactura)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFechaFactura)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotalFactura)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_factura, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val factura = facturas[position]

        holder.txtNumeroFactura.text = "Factura #${factura["id"]}"
        holder.txtCliente.text = "Cliente: ${factura["clienteNombre"]}"
        holder.txtFarmaceutico.text = "Farmacéutico: ${factura["farmaceuticoNombre"]}"
        holder.txtFecha.text = "Fecha: ${factura["fecha"]}"
        holder.txtTotal.text = "Total: $${factura["total"]}"

        holder.itemView.setOnClickListener {
            onFacturaClick(factura["id"]?.toInt() ?: 0)
        }
    }

    override fun getItemCount(): Int = facturas.size

    fun updateFacturas(newFacturas: List<Map<String, String>>) {
        facturas = newFacturas
        notifyDataSetChanged()
    }
}