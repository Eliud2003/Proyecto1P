package com.example.proyecto1p.medicinas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto1p.R

class MedicinasAdapter(private var medicinas: List<Medicinas>, context: Context) : RecyclerView.Adapter<MedicinasAdapter.MedicinasViewHolder>(){

    private val db: MedicinaDatabaseHelper = MedicinaDatabaseHelper(context)
    private var medicinasFullList: List<Medicinas> = medicinas.toList()

    class MedicinasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentPrincipioActivoTextView: TextView = itemView.findViewById(R.id.contentPrincipioActivoTextView)
        val contentConcentracionTextView: TextView = itemView.findViewById(R.id.contentConcentracionTextView)
        val contentPrecioTextView: TextView = itemView.findViewById(R.id.contentPrecioTextView)
        val contentStockTextView: TextView = itemView.findViewById(R.id.contentStockTextView)
        val contentFechaVencimientoTextView: TextView = itemView.findViewById(R.id.contentFechaVencimientoTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }//t

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinas_item, parent, false)
        return MedicinasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return medicinas.size
    }

    override fun onBindViewHolder(holder: MedicinasViewHolder, position: Int) {
        val medicina = medicinas[position]
        holder.titleTextView.text = medicina.nombre
        holder.contentPrincipioActivoTextView.text = medicina.principioActivo
        holder.contentConcentracionTextView.text = medicina.concentracion
        holder.contentPrecioTextView.text = "$${medicina.precio}"
        holder.contentStockTextView.text = medicina.stock
        holder.contentFechaVencimientoTextView.text = medicina.fechaVencimiento

        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateMedicinaActivity::class.java).apply {
                putExtra("medicina_id", medicina.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteMedicina(medicina.id)
            refreshData(db.getAllMedicinas())
            Toast.makeText(holder.itemView.context, "Medicina eliminada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    fun filterByName(query: String) {
        val filteredList = if (query.isEmpty()) {
            medicinasFullList
        } else {
            medicinasFullList.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
        medicinas = filteredList
        notifyDataSetChanged()
    }

    fun refreshData(newMedicinas: List<Medicinas>){
        medicinasFullList = newMedicinas.toList()
        medicinas = newMedicinas
        notifyDataSetChanged()
    }
}