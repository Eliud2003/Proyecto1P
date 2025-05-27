package com.example.proyecto1p.compras

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.R

class MedicinasSeleccionadasAdapter(
    private val medicinas: List<MedicinaSeleccionada>,
    private val onEliminarClick: (MedicinaSeleccionada) -> Unit
) : RecyclerView.Adapter<MedicinasSeleccionadasAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreMedicina)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioMedicina)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadMedicina)
        val txtSubtotal: TextView = itemView.findViewById(R.id.txtSubtotalMedicina)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminarMedicina)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medicina_seleccionada, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicina = medicinas[position]

        holder.txtNombre.text = medicina.nombre
        holder.txtPrecio.text = "$%.2f".format(medicina.precio)
        holder.txtCantidad.text = "Cant: ${medicina.cantidad}"
        holder.txtSubtotal.text = "$%.2f".format(medicina.subtotal)

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(medicina)
        }
    }

    override fun getItemCount(): Int = medicinas.size
}