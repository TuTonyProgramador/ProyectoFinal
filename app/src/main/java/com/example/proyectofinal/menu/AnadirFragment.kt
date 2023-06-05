package com.example.proyectofinal.menu

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.data.DatosAve
import com.example.proyectofinal.databinding.FragmentAnadirBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AnadirFragment : Fragment(R.layout.fragment_anadir) {
    // Declaración de variables
    private var _binding: FragmentAnadirBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()
    lateinit var storage: FirebaseStorage
    lateinit var imagen: ImageButton
    lateinit var foto: String
    lateinit var datosAve: DatosAve
    var contador = 0 // Variable contador declarada como miembro de la clase

    // Registro del ActivityResultContract para seleccionar una imagen desde la galería
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            uri -> // Devuelve la uri de la imagen seleccionada
        if (uri != null) {
            // Seleccionamos la imagen
            imagen.setImageURI(uri)
            Log.d("Galeria", "La imagen se ha seleccionado correctamente")

        } else {
            Log.d("Galeria", "No se ha seleccionado ninguna imagen")
        }
    }

    // Recuperar el correo del usuario
    var usuario = FirebaseAuth.getInstance().currentUser
    var correo = usuario?.email.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicialización de variables y vista
        foto = ""
        _binding = FragmentAnadirBinding.inflate(inflater, container, false)
        datosAve = DatosAve("", "", "", "","","", "", foto,"")
        val view = binding.root

        // Asignacion del imagenbutton
        imagen = binding.imageB
        imagen.setImageResource(R.drawable.imgdefecto)

        // Inicialización del storage de Firebase
        storage = Firebase.storage

        // Cuando pulsemos sobre el imageButton, se va a llamar al launcher (pickMedia) para que se lanze
        imagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Botón para registrar una nueva ave
        binding.BRegistrarAve.setOnClickListener {
            // Comprobación de que los campos no estén vacíos
            if (!(binding.tipoAve.text.isNullOrEmpty() && binding.numeroC.text.isNullOrEmpty() && binding.anioNacimiento.text.isNullOrEmpty() && binding.sexoA.text.isNullOrEmpty() && binding.numA.text.isNullOrEmpty() && binding.descri.text.isNullOrEmpty())) {
                binding.BRegistrarAve.isEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({
                    if (contador == 0) {
                        // Llamada al metodo de subir imagen
                        subirImagen()
                    }
                }, 1500)
            } else {
                // Si hay campos vacíos en la consulta, mostrar un mensaje de error
                Toast.makeText(requireActivity().applicationContext, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
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

    // Método para registrar el nuevo pájaro en la base de datos
    fun RegistrarAve() {
        // Asignación de la foto seleccionada a los datos del ave
        datosAve.Imagen = foto
        // Añadir los datos del ave a la base de datos
        db.collection("Canarios")
            // Crea un documento en la colección "Canarios" con un ID generado a partir del hashCode del número de criador
            .document(binding.numeroC.hashCode().toString())
            // Define los datos del ave como un mapa de valores
            .set(
                mapOf(
                    "Tipo" to binding.tipoAve.text.toString(),
                    "Numero_criador" to binding.numeroC.text.toString(),
                    "Anio_nac" to binding.anioNacimiento.text.toString(),
                    "Sexo" to binding.sexoA.text.toString(),
                    "Num_anilla" to binding.numA.text.toString(),
                    "Descripcion" to binding.descri.text.toString(),
                    "id" to binding.numeroC.hashCode().toString(),
                    "Imagen" to datosAve.Imagen,
                    "Usuario" to correo
                )
            )
            // Si se añaden los datos correctamente, muestra un mensaje en el log
            .addOnSuccessListener { documento ->
                Log.d(ContentValues.TAG, "Nuevo Pajaro añadido con id: ${binding.numeroC.hashCode()}")

                // Finaliza la actividad actual
                requireActivity().finish()
            }
            // Si se produce un error al añadir los datos, muestra un mensaje en el log
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error en la interseccion del nuevo registro")
            }
        // Volver a la actividad del PajarosActivity
        val pajarosA = Intent(activity, PajarosActivity::class.java)
        // Inicia la actividad de PajarosActivity y pasa el correo como parámetro
        startActivity(pajarosA)
        // Muestra un mensaje indicando que se ha añadido el ave correctamente
        Toast.makeText(requireActivity().applicationContext, "Nuevo pajaro añadido correctamente", Toast.LENGTH_SHORT).show()
    }

    // Metodo para subir una foto
    private fun subirImagen() {
        // Creamos la referencia al storage
        val storageRef = storage.reference

        // Referenciamos a donde queremos subir la imagen
        val rutaImagen = storageRef.child("profile/${binding.numeroC.hashCode()}/" + "canario.png")

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

        // Si se produce un error al subir la imagen, muestra un mensaje de error
        uploadTask.addOnFailureListener {
            Toast.makeText(requireActivity().applicationContext, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            // Si se sube la imagen correctamente, obtiene la URL de descarga y llama al método RegistrarAve() para añadir los datos del ave a la base de datos
        }.addOnSuccessListener { taskSnapshot ->
            // Para la descarga
            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                foto = it.toString()
                Log.i("UrlDescargarFoto", it.toString())

                // Llamada al método para registrar la ave después de que se haya obtenido la URL de descarga
                RegistrarAve()

            }.addOnFailureListener {
                Toast.makeText(requireActivity().applicationContext, "Error al obtener la URL de descarga", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Destrucción del Fragment
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}