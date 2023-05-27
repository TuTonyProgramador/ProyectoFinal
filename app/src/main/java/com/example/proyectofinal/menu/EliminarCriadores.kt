package com.example.proyectofinal.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentEliminarCriadoresBinding
import com.google.firebase.firestore.FirebaseFirestore


class EliminarCriadores : Fragment(R.layout.fragment_eliminar_criadores) {
    // Declaración de las variables
    private var _binding: FragmentEliminarCriadoresBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEliminarCriadoresBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.BEliminarCriador.setOnClickListener {
            eliminarCriador()
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

    private fun eliminarCriador() {
        val numeroCriador = binding.IntroNumeroC.text.toString()
        if (!numeroCriador.isNullOrEmpty()) {

            db.collection("Criadores")
            .whereEqualTo("NumeroCriador", numeroCriador)
                .get()
                .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }

                Toast.makeText(requireActivity().applicationContext, "Criador eliminado correctamente", Toast.LENGTH_SHORT).show()

                clearFocus() // Limpiar foco

                // Volver a la actividad del PajarosActivity
                val pajarosA = Intent(activity, PajarosActivity::class.java)

                // Volver a la actividad PajarosActivity
                startActivity(pajarosA)

                requireActivity().finish() // Finalizar la actividad actual

            }.addOnFailureListener { exception ->
                Toast.makeText(requireActivity().applicationContext, "Error al eliminar el criador: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireActivity().applicationContext, "El número de criador no puede estar vacío", Toast.LENGTH_SHORT).show()
        }
    }

    fun clearFocus() {
        binding.IntroNumeroC.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}