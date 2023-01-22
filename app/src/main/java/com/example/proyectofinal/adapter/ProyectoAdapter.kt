package com.example.proyectofinal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R

class ProyectoAdapter(private var consultarDatosAves:List<DatosAve>): RecyclerView.Adapter<ProyectoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProyectoViewHolder(layoutInflater.inflate(R.layout.lista_pajaros, parent, false))

    }

    override fun onBindViewHolder(holder: ProyectoViewHolder, position: Int) {
        val item:DatosAve = consultarDatosAves[position]
        holder.render(item)

    }

    override fun getItemCount(): Int = consultarDatosAves.size

}