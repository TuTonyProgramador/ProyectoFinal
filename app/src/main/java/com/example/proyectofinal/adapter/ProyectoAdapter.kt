package com.example.proyectofinal.adapter

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.data.DatosAve
import com.example.proyectofinal.viewHolder.ProyectoViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage



class ProyectoAdapter(private var consultarDatosAves:ArrayList<DatosAve>): RecyclerView.Adapter<ProyectoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyectoViewHolder {
        // Infla el layout de cada item de la lista y lo asigna a un ViewHolder
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProyectoViewHolder(layoutInflater.inflate(R.layout.lista_pajaros, parent, false))
    }

    override fun onBindViewHolder(holder: ProyectoViewHolder, position: Int) {
        // Obtiene la cantidad de elementos en la lista
        var size = consultarDatosAves.size

        // Obtiene una instancia de la base de datos Firestore
        val db = FirebaseFirestore.getInstance()

        // Obtiene el usuario autenticado
        var autentication = FirebaseAuth.getInstance()

        // Obtiene el objeto DatosAve correspondiente a la posición actual en la lista
        val item: DatosAve = consultarDatosAves[position]

        // Llama al método render() del ViewHolder para mostrar los datos del objeto DatosAve en la vista correspondiente
        holder.render(item)

        // Configura un listener para el evento "click" en el item de la lista
        holder.itemView.setOnClickListener {
            // Define el título y el mensaje del diálogo de confirmación para borrar el pájaro
            val title = "Confirmacion"
            val content = "¿Desea borrar el pajaro registrada?"

            // Obtiene el ID del pájaro correspondiente al objeto DatosAve actual
            var id = item.id

            // Crea el diálogo de confirmación para borrar el pájaro
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(title)
            builder.setMessage(content)

            // Configura el botón "Eliminar" del diálogo para borrar el pájaro de la base de datos y el almacenamiento de Firebase, y actualizar la lista
            builder.setPositiveButton("Eliminar") { dialog, which ->
                // Elimina el registro de la base de datos Firestore
                db.collection("Canarios").document(id).delete()

                // Elimina la imagen del almacenamiento de Firebase
                val imagenUrl = item.Imagen
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imagenUrl)
                storageRef.delete().addOnSuccessListener {
                    // Actualiza la lista de pájaros eliminando el elemento correspondiente y notificando el cambio
                    val posicion = consultarDatosAves.indexOf(item)
                    consultarDatosAves.removeAt(posicion)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, size)
                    notifyDataSetChanged()
                }.addOnFailureListener { exception ->
                    Toast.makeText(it.context,"Ocurrió un error al eliminar el pajaro", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error al eliminar la imagen del Storage: $exception")
                }

                Toast.makeText(it.context,"El pajaro se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
            }

            // Configura el botón "Cancelar" del diálogo para cerrarlo sin borrar el pájaro
            builder.setNegativeButton("Cancelar") { dialog, which ->
                Toast.makeText(it.context,"El pajaro no se ha eliminado", Toast.LENGTH_SHORT).show()
            }

            // Crea y muestra el diálogo de confirmación
            val dialog = builder.create()
            dialog.show()
        }

        // Descarga la imagen correspondiente al pájaro desde el almacenamiento de Firebase
        autentication.currentUser?.let {
            val storage = FirebaseStorage.getInstance()
            val refguardado = storage.getReference("profile/")
            val refimagen = refguardado.child("${consultarDatosAves[position].id}/" + "canario.png")

            // Descargamos la imagen desde el Storage y la mostramos en el ImageView correspondiente
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