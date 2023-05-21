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
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentAnadirCriadoresBinding
import com.example.proyectofinal.databinding.FragmentSoporteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AnadirCriadores : Fragment(R.layout.fragment_anadir_criadores) {
    // Definimos una propiedad para el binding
    private var _binding: FragmentAnadirCriadoresBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    // Recuperar el correo del usuario
    var usuario = FirebaseAuth.getInstance().currentUser
    var correo = usuario?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnadirCriadoresBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.BRegistrarCriador.setOnClickListener {
            RegistrarCriadores()
        }

        return view
    }

    fun RegistrarCriadores() {
        if (!(binding.numeroCriador.text.isNullOrEmpty())) {
            db.collection("Criadores")
                // Define los datos del ave como un mapa de valores
                .add(
                    mapOf(
                        "NumeroCriador" to binding.numeroCriador.text.toString(),
                        "Nombre" to binding.nCriador.text.toString(),
                        "Apellidos" to binding.aCriador.text.toString(),
                        "Localidad" to binding.lCriador.text.toString(),
                        "Asociacion" to binding.asCriador.text.toString(),
                        "Federacion" to binding.fCriador.text.toString(),
                    )
                )
                // Si se añaden los datos correctamente, muestra un mensaje en el log
                .addOnSuccessListener { documento ->
                    Log.d(ContentValues.TAG, "Nuevo Pajaro añadido con id: ${binding.numeroCriador.hashCode()}")
                }
                // Si se produce un error al añadir los datos, muestra un mensaje en el log
                .addOnFailureListener {
                    Log.d(ContentValues.TAG, "Error en la interseccion del nuevo registro")
                }
            // Volver a la actividad del homeFragment
            val homeFragment = Intent(activity, PajarosActivity::class.java)
            // Llamar al correo
            homeFragment.putExtra("emailUsuario", correo)
            // Llamar al nombre
            homeFragment.putExtra("Nombre", correo)
            // Inicia la actividad de PajarosActivity y pasa el correo como parámetro
            startActivity(homeFragment)

        } else {
            // Si hay campos vacíos en la consulta, mostrar un mensaje de error
            Toast.makeText(requireActivity().applicationContext, "El numero de criador no puede estar vacio", Toast.LENGTH_SHORT).show()
        }
    }
}