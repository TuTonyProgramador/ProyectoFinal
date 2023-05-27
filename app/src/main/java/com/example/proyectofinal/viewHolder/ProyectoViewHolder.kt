package com.example.proyectofinal.viewHolder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.data.DatosAve
import com.example.proyectofinal.databinding.ListaPajarosBinding


class ProyectoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ListaPajarosBinding.bind(view)

    // Se define una variable `imagen` que hace referencia a la vista `ImageView` con id `imagenLista`
    // definida en el archivo de diseño `lista_pajaros.xml`
    val imagen = itemView.findViewById<ImageView>(R.id.imagenLista)

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
        // Mostrar numero de anilla del canario
        binding.DescripcionAve.text = ProyectoModel.Descripcion

    }
}