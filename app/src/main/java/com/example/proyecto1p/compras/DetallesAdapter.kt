package com.example.proyecto1p.compras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.R

class DetallesAdapter(
    private val detalles: List<Map<String, String>>
) : RecyclerView.Adapter<DetallesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMedicina: TextView = itemView.findViewById(R.id.txtMedicinaDetalle)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadDetalle)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioDetalle)
        val txtSubtotal: TextView = itemView.findViewById(R.id.txtSubtotalDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detalle_factura, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detalle = detalles[position]

        holder.txtMedicina.text = detalle["medicinaNombre"]
        holder.txtCantidad.text = "Cant: ${detalle["cantidad"]}"
        holder.txtPrecio.text = "$${detalle["precioUnitario"]}"
        holder.txtSubtotal.text = "$${detalle["subtotal"]}"
    }

    override fun getItemCount(): Int = detalles.size
}