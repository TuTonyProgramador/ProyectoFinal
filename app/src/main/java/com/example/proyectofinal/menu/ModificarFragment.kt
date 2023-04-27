package com.example.proyectofinal.menu

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentModificarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class ModificarFragment : Fragment(R.layout.fragment_modificar) {
    private var _binding: FragmentModificarBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()
    lateinit var documentId: String
    var obtenerFoto: String? = null

    // Recuperar el correo
    var usuario = FirebaseAuth.getInstance().currentUser
    var correo = usuario?.email.toString()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModificarBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.BConsultar.setOnClickListener {
            if (!(binding.MAnioN.text.isNullOrEmpty() && binding.MNumAni.text.isNullOrEmpty() && binding.MNumC.text.isNullOrEmpty() && binding.MSexo.text.isNullOrEmpty() && binding.MTipoAve.text.isNullOrEmpty())) {
                db.collection("Canarios")
                    .whereEqualTo("Numero_criador", binding.MNumC.text.toString())
                    .whereEqualTo("Num_anilla", binding.MNumAni.text.toString())
                    .whereEqualTo("Anio_nac", binding.MAnioN.text.toString())
                    .get()
                    .addOnSuccessListener {
                        it.forEach {
                            Log.d(TAG, "Documento encontrado: " + it.data)
                            binding.MNumC.setText(it.get("Numero_criador") as String?)
                            binding.MNumAni.setText(it.get("Num_anilla") as String?)
                            binding.MAnioN.setText(it.get("Anio_nac") as String?)
                            binding.MSexo.setText(it.get("Sexo") as String?)
                            binding.MTipoAve.setText(it.get("Tipo") as String?)
                            obtenerFoto = (it.get("Imagen") as String?)
                            correo = (it.get("Usuario") as String)
                            documentId = it.id
                        }
                    }

            } else {
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Algun campo está vacio",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.BModificar.setOnClickListener {
            var auth = FirebaseAuth.getInstance()

            auth.currentUser?.let {
                val updatesMap = hashMapOf(
                    "Numero_criador" to binding.MNumC.text.toString(),
                    "Num_anilla" to binding.MNumAni.text.toString(),
                    "Anio_nac" to binding.MAnioN.text.toString(),
                    "Sexo" to binding.MSexo.text.toString(),
                    "Tipo" to binding.MTipoAve.text.toString(),
                    "Imagen" to obtenerFoto,
                    "Usuario" to correo
                )
                db.collection("Canarios").document(documentId).set(updatesMap)
                    .addOnSuccessListener {
                        // El registro se ha actualizado correctamente
                    }
                    .addOnFailureListener {
                        // Ha ocurrido un error al actualizar el registro
                    }


                // Volver a la actividad del homeFragment
                val homeFragment = Intent(activity, PajarosActivity::class.java)
                // Llamar al correo
                homeFragment.putExtra("emailUsuario", correo)
                startActivity(homeFragment)
            }
        }

        return view

    }

    // Destruir la actividad
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}