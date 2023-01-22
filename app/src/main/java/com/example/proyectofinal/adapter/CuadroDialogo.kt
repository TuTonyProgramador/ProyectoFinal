package com.example.proyectofinal.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.proyectofinal.databinding.FragmentAnadirBinding
import com.google.firebase.firestore.FirebaseFirestore

class CuadroDialogo: DialogFragment() {
    val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentAnadirBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val title = "Confirmacion"
            val content = "¿Desea Borrar el Ave Registrada?"
            val builder: AlertDialog.Builder= AlertDialog.Builder(requireActivity())
            builder.setTitle(title).setMessage(content)
                .setPositiveButton(android.R.string.ok)  {dialog, which ->
                    // Eliminamos el registro de la base de datos
                    db.collection("Canarios")
                        .document(binding.numeroC.hashCode().toString())
                        .delete()

                    Toast.makeText(requireActivity().applicationContext,"Has Eliminado el Ave", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(android.R.string.cancel) {dialog, which ->
                    Toast.makeText(requireActivity().applicationContext, "Se ha cancelado la operacion", Toast.LENGTH_SHORT).show()
                }
            //Devuelve un AlertDialog
            //tal y como lo hemos definido en el builder
            return builder.create()
        }
    }
}