package com.example.proyectofinal.menu

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentAnadirBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AnadirFragment : Fragment(R.layout.fragment_anadir) {
    private var _binding: FragmentAnadirBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()
    lateinit var storage: FirebaseStorage
    lateinit var imagen: ImageButton

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        // Devuelve la uri de la imagen seleccionada
            uri ->
        if(uri!=null) {
            // Seleccionamos la imagen
            imagen.setImageURI(uri)

            Log.d("Galeria", "La imagen se ha seleccionado correctamente")

        } else{
            Log.d("Galeria", "No se ha seleccionado ninguna imagen")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnadirBinding.inflate(inflater, container, false)
        val view = binding.root
        // Asignacion del imagenbutton
        imagen = binding.imageB

        // Inicializacion del storage
        storage = Firebase.storage

        binding.BRegistrarAve.setOnClickListener {
            RegistrarAve()
            // Llamada al metodo de subir imagen
            subirImagen()
        }

        // Cuando pulsemos sobre el imageButton, se va a llamar al launcher (pickMedia) para que se lanze
        imagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    private fun subirImagen() {
        // Creamos la referencia al storage
        val storageRef = storage.reference

        // Referenciamos a donde queremos subir la imagen
        // System.currentTimeMillis() para que no se repita el nombre de la imagen, siempre va a salir distinto
        val rutaImagen = storageRef.child("profile/" + binding.numeroC.hashCode() + "/canario.PNG")

        // Cogemos la imagen y la transformamos en bitmap(imagen en bits)
        val bitmap = (imagen.drawable as BitmapDrawable).bitmap
        // Canal de comunicacion para mandar unos datos
        val baos = ByteArrayOutputStream()
        // Comprime la imagen
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        // Tranformacion en array de byte
        val data = baos.toByteArray()

        // Poner los byte de la imagen en la ruta
        var uploadTask = rutaImagen.putBytes(data)

    }

}