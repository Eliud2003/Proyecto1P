package com.example.proyecto1p.Farmaceuticos

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

class FarmaceuticosAdapter(private var farmaceuticos: List<Farmaceuticos>, context: Context) : RecyclerView.Adapter<FarmaceuticosAdapter.FarmaceuticosViewHolder>(){

    private val db: FarmaceuticoDatabaseHelper = FarmaceuticoDatabaseHelper(context)
    private var farmaceuticosFullList: List<Farmaceuticos> = farmaceuticos.toList()

    class FarmaceuticosViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentCedulaTextView: TextView = itemView.findViewById(R.id.contentCedulaTextView)
        val contentTelefonoTextView: TextView = itemView.findViewById(R.id.contentTelefonoTextView)
        val contentEdadTextView: TextView = itemView.findViewById(R.id.contentEdadTextView)
        val contentExperienciaTextView: TextView = itemView.findViewById(R.id.contentExperienciaTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmaceuticosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.farmaceuticos_item,parent,false)
        return FarmaceuticosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return farmaceuticos.size
    }

    override fun onBindViewHolder(holder: FarmaceuticosViewHolder, position: Int) {
        val farmaceutico = farmaceuticos[position]
        holder.titleTextView.text = farmaceutico.nombreCompleto
        holder.contentCedulaTextView.text = farmaceutico.cedula
        holder.contentTelefonoTextView.text = farmaceutico.telefono
        holder.contentEdadTextView.text = farmaceutico.edad
        holder.contentExperienciaTextView.text = farmaceutico.añosExperiencia
        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("farmaceutico_id", farmaceutico.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            db.deleteFarmaceutico(farmaceutico.id)
            refreshData(db.getAllFarmaceuticos())
            Toast.makeText(holder.itemView.context,"Farmaceutico eliminado con exito", Toast.LENGTH_SHORT).show()
        }
    }

    fun filterByName(query: String) {
        val filteredList = if (query.isEmpty()) {
            farmaceuticosFullList
        } else {
            farmaceuticosFullList.filter {
                it.nombreCompleto.contains(query, ignoreCase = true)
            }
        }
        farmaceuticos = filteredList
        notifyDataSetChanged()
    }

    fun refreshData(newFarmaceutico: List<Farmaceuticos>){
        farmaceuticosFullList = newFarmaceutico.toList()
        farmaceuticos = newFarmaceutico
        notifyDataSetChanged()
    }
}