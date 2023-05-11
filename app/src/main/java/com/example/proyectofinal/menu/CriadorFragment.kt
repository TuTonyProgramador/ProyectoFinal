package com.example.proyectofinal.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentCriadorBinding
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
            // Consultar el criador en la base de datos
            db.collection("Criadores").document(binding.NumCriador.text.toString())

            // Lanzar la actividad DatosCriadorActivity y pasar el número de criador como parámetro
            val intent = Intent(activity, DatosCriadorActivity::class.java).apply {
                putExtra("NumCriador", binding.NumCriador.text.toString())
            }
            startActivity(intent)

            // Llamada al método para limpiar el foco
            clearFocus()
        }

        // Devuelve la vista del fragmento
        return view
    }

    // Liberar los recursos asociados al binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Método para limpiar el foco
    fun clearFocus() {
        binding.NumCriador.setText("")
    }
}