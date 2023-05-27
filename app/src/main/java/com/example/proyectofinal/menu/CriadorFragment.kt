package com.example.proyectofinal.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentCriadorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CriadorFragment : Fragment(R.layout.fragment_criador) {
    // Declaración de las variables
    private var _binding: FragmentCriadorBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        _binding = FragmentCriadorBinding.inflate(inflater, container, false)
        val view = binding.root

        // Configuración del botón para consultar el criador

        binding.BConsultarCriador.setOnClickListener {
            if (!binding.NumCriador.text.isNullOrEmpty()) {
                val numCriador = binding.NumCriador.text.toString()

                // Consultar el criador en la base de datos
                db.collection("Criadores")
                    .whereEqualTo("NumeroCriador", numCriador)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            // El criador existe, lanzar la actividad DatosCriadorActivity y pasar el número de criador como parámetro
                            val intent = Intent(activity, DatosCriadorActivity::class.java).apply {
                                putExtra("NumCriador", numCriador)
                            }
                            startActivity(intent)
                        } else {
                            // El criador no existe, mostrar un mensaje de error
                            Toast.makeText(requireActivity().applicationContext, "El criador introducido no está registrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Ocurrió un error al consultar la base de datos
                        Toast.makeText(requireActivity().applicationContext, "Error al consultar el criador: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                // Llamada al método para limpiar el foco
                clearFocus()
            } else {
                // Si no se ha introducido ningún criador, mostrar un mensaje de error
                Toast.makeText(requireActivity().applicationContext, "No ha introducido ningún criador", Toast.LENGTH_SHORT).show()
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Realizar la navegación deseada cuando se presione el botón de retroceso
                val homeFragment = Intent(activity, PajarosActivity::class.java)
                startActivity(homeFragment)
                requireActivity().finish()
            }
        })

        // Devuelve la vista del fragmento
        return view
    }

    // Método para limpiar el foco
    fun clearFocus() {
        binding.NumCriador.setText("")
    }

    // Liberar los recursos asociados al binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}