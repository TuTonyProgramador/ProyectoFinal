package com.example.proyectofinal.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.databinding.ListaPajarosBinding


class ProyectoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ListaPajarosBinding.bind(view)

    fun render(ProyectoModel: DatosAve) {
        // Mostrar el tipo de canario
        binding.tipo.text = ProyectoModel.Tipo
        // Mostrar el numero de criador del canario
        binding.NumeroCriador.text = ProyectoModel.Numero_criador
        // Mostrar año de nacimiento del canario
        binding.AnioNacimiento.text = ProyectoModel.Anio_nac
        // Mostrar sexo del canario
        binding.Sexo.text = ProyectoModel.Sexo
        // Mostrar numero de anilla del canario
        binding.NumeroAnilla.text = ProyectoModel.Num_anilla

        // Mostrar imagen del canario
        Glide.with(binding.imagenLista.context).load(ProyectoModel.Imagen).into(binding.imagenLista)

    }

}