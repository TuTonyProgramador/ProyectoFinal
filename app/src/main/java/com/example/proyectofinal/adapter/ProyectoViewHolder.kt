package com.example.proyectofinal.adapter

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.databinding.ListaPajarosBinding
import com.google.firebase.firestore.FirebaseFirestore


class ProyectoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ListaPajarosBinding.bind(view)
    val db = FirebaseFirestore.getInstance()

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

        // Eliminar pajaro
        itemView.setOnClickListener {
            val title = "Confirmacion"
            val content = "¿Desea borrar el pajaro registrada?"

            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle(title)
            builder.setMessage(content)

            builder.setPositiveButton("Eliminar") { dialog, which ->
                // Eliminamos el registro de la base de datos
                db.collection("Canarios").whereEqualTo("id", ProyectoModel.id.hashCode()).get()
                    .addOnSuccessListener { pajaro ->
                        for (document in pajaro) {
                            db.collection("Canarios")
                                .document(binding.NumeroCriador.text.hashCode().toString())
                                .delete()
                        }
                    }
                Log.d("Eliminar", "Eliminado")
                Toast.makeText(it.context,"El pajaro se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                Toast.makeText(it.context,"El pajaro no se ha eliminado", Toast.LENGTH_SHORT).show()
            }

            // Create the dialog
            val dialog = builder.create()

            // Show the dialog
            dialog.show()

        }

    }

}