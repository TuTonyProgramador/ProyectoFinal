package com.example.proyectofinal.adapter

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage



class ProyectoAdapter(private var consultarDatosAves:ArrayList<DatosAve>): RecyclerView.Adapter<ProyectoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProyectoViewHolder(layoutInflater.inflate(R.layout.lista_pajaros, parent, false))

    }

    override fun onBindViewHolder(holder: ProyectoViewHolder, position: Int) {
        var size = consultarDatosAves.size

        val db = FirebaseFirestore.getInstance()
        // Obtener el usuario autentificado
        var autentication = FirebaseAuth.getInstance()
        val item:DatosAve = consultarDatosAves[position]
        holder.render(item)

        // Eliminar pajaro
        holder.itemView.setOnClickListener {
            val title = "Confirmacion"
            val content = "¿Desea borrar el pajaro registrada?"
            // id del pajaro
            var id = item.id

            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(title)
            builder.setMessage(content)

            builder.setPositiveButton("Eliminar") { dialog, which ->
                // Eliminamos el registro de la base de datos
                db.collection("Canarios").document(id).delete()

                Toast.makeText(it.context,"El pajaro se ha eliminado correctamente", Toast.LENGTH_SHORT).show()

                val posicion = consultarDatosAves.indexOf(item)
                consultarDatosAves.removeAt(posicion)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, size)
                notifyDataSetChanged()
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                Toast.makeText(it.context,"El pajaro no se ha eliminado", Toast.LENGTH_SHORT).show()
            }

            // Create the dialog
            val dialog = builder.create()

            // Show the dialog
            dialog.show()

        }

        // Descargar imagen
        autentication.currentUser?.let {
            val storage = FirebaseStorage.getInstance()
            val refguardado = storage.getReference("profile/")

            val refimagen = refguardado.child("${consultarDatosAves[position].id}/" + "canario.png")

            refimagen.getBytes(1024 * 1024).addOnSuccessListener {
                val foto = BitmapFactory.decodeByteArray(it, 0, it.size)
                holder.imagen.setImageBitmap(foto)
                Log.i("ImagenLista", "Imagen cargada")
            }.addOnFailureListener {
                // Handle any errors
                Log.i("ImagenLista2", "Error al cargar la imagen")
            }
        }
    }

    override fun getItemCount(): Int = consultarDatosAves.size

}