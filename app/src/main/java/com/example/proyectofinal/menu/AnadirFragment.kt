package com.example.proyectofinal.menu

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.adapter.CuadroDialogo
import com.example.proyectofinal.adapter.DatosAve
import com.example.proyectofinal.databinding.FragmentAnadirBinding
import com.google.firebase.firestore.FirebaseFirestore

class AnadirFragment : Fragment(R.layout.fragment_anadir) {
    private var _binding: FragmentAnadirBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnadirBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.BRegistrarAve.setOnClickListener {
            RegistrarAve()
        }

        return view

    }

    // Destruir la actividad
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun RegistrarAve() {
        if(!(binding.tipoAve.text.isNullOrEmpty() && binding.numeroC.text.isNullOrEmpty() && binding.anioNacimiento.text.isNullOrEmpty() && binding.sexoA.text.isNullOrEmpty() && binding.numA.text.isNullOrEmpty())){
            db.collection("Canarios")
                .document(binding.numeroC.hashCode().toString())
                .set(mapOf("Tipo" to binding.tipoAve.text.toString(),
                    "Numero_criador" to binding.numeroC.text.toString(),
                    "Anio_nac" to binding.anioNacimiento.text.toString(),
                    "Sexo" to binding.sexoA.text.toString(),
                    "Num_anilla" to binding.numA.text.toString(),
                    "id" to binding.numeroC.hashCode().toString()))

                .addOnSuccessListener {
                        documento -> Log.d(ContentValues.TAG, "Nuevo Pajaro añadido con id: ${binding.numeroC.hashCode()}")
                }
                .addOnFailureListener{
                    Log.d(ContentValues.TAG, "Error en la interseccion del nuevo registro")
                }

            // Volver a la actividad del homeFragment
            val homeFragment = Intent(activity, PajarosActivity::class.java)
            startActivity(homeFragment)

            Toast.makeText(requireActivity().applicationContext, "Nuevo pajaro añadido correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity().applicationContext, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
        }

    }

}