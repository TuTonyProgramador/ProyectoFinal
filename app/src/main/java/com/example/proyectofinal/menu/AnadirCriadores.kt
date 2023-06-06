package com.example.proyectofinal.menu

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentAnadirCriadoresBinding
import com.google.firebase.firestore.FirebaseFirestore


class AnadirCriadores : Fragment(R.layout.fragment_anadir_criadores) {
    // Definimos una propiedad para el binding
    private var _binding: FragmentAnadirCriadoresBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnadirCriadoresBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.BRegistrarCriador.setOnClickListener {
            RegistrarCriadores()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Realizar la navegación deseada cuando se presione el botón de retroceso
                val homeFragment = Intent(activity, PajarosActivity::class.java)
                startActivity(homeFragment)
                requireActivity().finish()
            }
        })

        return view
    }

    fun RegistrarCriadores() {
        val numeroCriador = binding.numeroCriador.text.toString()

        if (numeroCriador.isNotEmpty()) {
            // Realizar la consulta para comprobar si el criador ya existe
            db.collection("Criadores")
                .whereEqualTo("NumeroCriador", numeroCriador)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        // El criador no existe, se puede agregar a la base de datos
                        val nuevoCriador = mapOf(
                            "NumeroCriador" to numeroCriador,
                            "Nombre" to binding.nCriador.text.toString(),
                            "Apellidos" to binding.aCriador.text.toString(),
                            "Localidad" to binding.lCriador.text.toString(),
                            "Asociacion" to binding.asCriador.text.toString(),
                            "Federacion" to binding.fCriador.text.toString()
                        )

                        db.collection("Criadores")
                            .add(nuevoCriador)
                            .addOnSuccessListener { documento ->
                                Log.d(ContentValues.TAG, "Nuevo Criador añadido con id: ${documento.id}")
                                // Volver a la actividad del PajarosA
                                val PajarosA = Intent(activity, PajarosActivity::class.java)
                                // Inicia la actividad de PajarosActivity y pasa el correo como parámetro
                                startActivity(PajarosA)
                                // Finaliza la actividad actual
                                requireActivity().finish()
                            }
                            .addOnFailureListener {
                                Log.d(ContentValues.TAG, "Error en la interseccion del nuevo registro")
                            }
                    } else {
                        // El criador ya existe en la base de datos
                        Toast.makeText(requireActivity().applicationContext, "Este criador ya esta registrado", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Si el número de criador está vacío, mostrar un mensaje de error
            Toast.makeText(requireActivity().applicationContext, "El número de criador no puede estar vacío", Toast.LENGTH_SHORT).show()
        }
    }

}