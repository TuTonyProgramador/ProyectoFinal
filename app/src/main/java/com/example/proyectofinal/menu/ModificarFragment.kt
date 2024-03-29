package com.example.proyectofinal.menu

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
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
    private var obtenerFoto: String? = null

    // Recuperar el correo
    var usuario = FirebaseAuth.getInstance().currentUser
    var correo = usuario?.email.toString()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar la vista del Fragment con el layout correspondiente
        _binding = FragmentModificarBinding.inflate(inflater, container, false)
        val view = binding.root

        // Poner invisibles los edittext
        binding.MTipoAve.isVisible = false
        binding.MSexo.isVisible = false
        binding.descripci.isVisible = false

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Realizar la navegación deseada cuando se presione el botón de retroceso
                val homeFragment = Intent(activity, PajarosActivity::class.java)
                startActivity(homeFragment)
                requireActivity().finish()
            }
        })

        // Configurar el botón de consulta
        binding.BConsultar.setOnClickListener {
            // Obtiene el email del usuario actual
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

            // Verificar que no haya campos vacíos en la consulta
            if (!(binding.MAnioN.text.isNullOrEmpty() || binding.MNumAni.text.isNullOrEmpty() || binding.MNumC.text.isNullOrEmpty())) {
                // Realizar la consulta a Firestore para buscar el documento correspondiente
                db.collection("Canarios")
                    .whereEqualTo("Numero_criador", binding.MNumC.text.toString())
                    .whereEqualTo("Num_anilla", binding.MNumAni.text.toString())
                    .whereEqualTo("Anio_nac", binding.MAnioN.text.toString())
                    .whereEqualTo("Usuario", currentUserEmail)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            // Si se encuentra el documento, mostrar los datos en los campos correspondientes y almacenar el id del documento y la imagen en variables
                            querySnapshot.forEach {
                                Log.d(TAG, "Pajaro encontrado: " + it.data)
                                binding.MNumC.setText(it.get("Numero_criador") as String?)
                                binding.MNumAni.setText(it.get("Num_anilla") as String?)
                                binding.MAnioN.setText(it.get("Anio_nac") as String?)
                                binding.MSexo.setText(it.get("Sexo") as String?)
                                binding.MTipoAve.setText(it.get("Tipo") as String?)
                                binding.descripci.setText(it.get("Descripcion") as String?)
                                obtenerFoto = (it.get("Imagen") as String?)
                                correo = (it.get("Usuario") as String)
                                documentId = (it.get("id") as String)
                            }

                            // Poner visibles los edittext
                            binding.MTipoAve.isVisible = true
                            binding.MSexo.isVisible = true
                            binding.descripci.isVisible = true

                            // Hacer visible el boton de modificar
                            binding.BModificar.visibility = View.VISIBLE

                        } else {
                            // Si no se encuentra el documento, mostrar un mensaje de error y ocultar el botón de modificar
                            Toast.makeText(requireActivity().applicationContext, "El pájaro introducido no está registrado", Toast.LENGTH_SHORT).show()
                            binding.BModificar.visibility = View.GONE
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Si se produce un error al realizar la consulta, mostrar un mensaje de error y ocultar el botón de modificar
                        Log.e(TAG, "Error al obtener el documento", exception)
                        Toast.makeText(requireActivity().applicationContext,"Se ha producido un error al buscar el pájaro", Toast.LENGTH_SHORT).show()
                        binding.BModificar.visibility = View.GONE
                    }

            } else {
                // Si hay campos vacíos en la consulta, mostrar un mensaje de error
                Toast.makeText(requireActivity().applicationContext, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de modificar
        binding.BModificar.setOnClickListener {
            // Desactivar el botón de consulta
            binding.BConsultar.isEnabled = false

            // Obtener instancia de FirebaseAuth
            var auth = FirebaseAuth.getInstance()

            // Si hay un usuario actualmente autenticado
            auth.currentUser?.let {
                // Crear mapa de actualizaciones con los datos del formulario
                val updatesMap = hashMapOf(
                    "Numero_criador" to binding.MNumC.text.toString(),
                    "Num_anilla" to binding.MNumAni.text.toString(),
                    "Anio_nac" to binding.MAnioN.text.toString(),
                    "Sexo" to binding.MSexo.text.toString(),
                    "Tipo" to binding.MTipoAve.text.toString(),
                    "Descripcion" to binding.descripci.text.toString(),
                    "id" to documentId,
                    "Imagen" to obtenerFoto,
                    "Usuario" to correo
                )
                // Actualizar el documento del pájaro en Firestore con las actualizaciones
                db.collection("Canarios").document(documentId).set(updatesMap)
                    .addOnSuccessListener {
                        // Si la actualización fue exitosa, mostrar un mensaje de éxito
                        Toast.makeText(requireActivity().applicationContext, "El pájaro ha sido actualizado correctamente", Toast.LENGTH_SHORT).show()

                        // Finalizar la actividad actual
                        requireActivity().finish()
                    }
                    .addOnFailureListener {
                        // Si la actualización falló, mostrar un mensaje de error
                        Toast.makeText(requireActivity().applicationContext, "Error al actualizar el pájaro", Toast.LENGTH_SHORT).show()
                    }


                // Volver a la actividad del homeFragment
                val PajarosA = Intent(activity, PajarosActivity::class.java)
                startActivity(PajarosA)
            }
        }

        // Devuelve la vista del fragmento
        return view
    }

    // Destruir la vista del fragmento cuando se destruye la actividad
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}