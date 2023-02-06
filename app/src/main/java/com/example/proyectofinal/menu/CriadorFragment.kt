package com.example.proyectofinal.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectofinal.DatosCriadorActivity
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentCriadorBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CriadorFragment : Fragment(R.layout.fragment_criador) {
    private var _binding: FragmentCriadorBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCriadorBinding.inflate(inflater, container, false)
        val view = binding.root

        // Boton para consultar el criador
        binding.BConsultarCriador.setOnClickListener {
            db.collection("Criadores").document(binding.NumCriador.text.toString())

            val intent = Intent(activity, DatosCriadorActivity::class.java).apply {
                putExtra("NumCriador", binding.NumCriador.text.toString())
            }
            startActivity(intent)

            // Llamada al metodo para limpiar el foco
            clearFocus()
        }
        return view
    }

    // Destruir la actividad
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Al pulsar sobre el boton consultar, se limpia
    fun clearFocus() {
        binding.NumCriador.setText("")
    }
}