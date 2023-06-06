package com.example.proyectofinal.menu

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.data.DatosNotas
import com.example.proyectofinal.databinding.FragmentCalendarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class CalendarioFragment : Fragment(R.layout.fragment_calendario) {

    // Definimos una propiedad para el binding
    private var _binding: FragmentCalendarioBinding? = null
    // Usamos delegación de propiedades para crear una propiedad "binding" no nula
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()

    var calendar: Calendar? = null

    // Variable para almacenar la fecha seleccionada
    private var selectedDate: Calendar? = null

    var dia : Int = 0
    var mes : Int = 0
    var anio : Int = 0
    var fecha : String = ""

    private var listaNotas = ArrayList<DatosNotas>()

    // Recuperar el correo del usuario
    var usuario = FirebaseAuth.getInstance().currentUser
    var correo = usuario?.email.toString()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarioBinding.inflate(inflater, container, false)
        val view = binding.root

        listaNotas = ArrayList()

        // Configuración del listener para el cambio de fecha en el calendario
        binding.calendarView.setOnDateChangeListener { view, anio, mes, dia ->
            calendar = Calendar.getInstance() // Obtiene la fecha y hora actual
            selectedDate = Calendar.getInstance().apply {
                set(anio, mes, dia) // Establece la fecha seleccionada en el calendario
            }
            if (selectedDate!!.before(calendar)) {
                Toast.makeText(requireContext(), "No puedes agregar notas, días anteriores al actual", Toast.LENGTH_SHORT).show()
            } else {
                showNoteDialog(requireContext(), anio, mes, dia)
                fecha  = dia.toString() + mes.toString() + anio.toString()
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

        return view
    }

    // Método para mostrar el diálogo de agregar nota
    private fun showNoteDialog(context: Context, year: Int, month: Int, dayOfMonth: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Añadir nota $dayOfMonth/${month + 1}/$year")
        val input = EditText(context)
        builder.setView(input)

        // Acciones del botón "Guardar" del diálogo
        builder.setPositiveButton("Guardar") { dialog, which ->
            val nota = input.text.toString().toLowerCase().trim() // Obtener la nota y eliminar espacios en blanco al inicio y al final

            if (nota.isNotEmpty()) {
                if (nota.length > 33) {
                    Toast.makeText(requireContext(), "Has superado el límite de caracteres permitidos", Toast.LENGTH_SHORT).show()
                } else {
                    db.collection("Eventos").document(nota.hashCode().toString())
                        .set(
                            mapOf(
                                "Nota" to nota,
                                "fecha" to fecha,
                                "Usuario" to correo,
                                "id" to nota.hashCode().toString()
                            )
                        )
                        .addOnSuccessListener {
                            Toast.makeText(context, "Nota guardada", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al guardar nota", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(context, "La nota está vacía", Toast.LENGTH_SHORT).show()
            }
        }
        // Acciones del botón "Cancelar" del diálogo
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.cancel()
        }
        // Acciones del botón "Ver notas" del diálogo
        builder.setNeutralButton("Ver notas") { dialog, which ->
            showNotesForDate(context)
        }

        builder.show()
    }



    // Método para mostrar las notas de una fecha específica
    private fun showNotesForDate(context: Context) {
        // Obtener la fecha seleccionada
        val selectedDate = selectedDate ?: return

        // Crear una referencia a la colección "Eventos"
        db.collection("Eventos")
            .whereEqualTo("fecha", fecha)
            .whereEqualTo("Usuario", correo)
            .get()
            .addOnSuccessListener { documents ->
                // Limpiar la lista de notas antes de agregar las nuevas
                listaNotas.clear()

                // Recorrer los documentos encontrados y agregar las notas a la lista
                for (document in documents) {
                    val nota = document.getString("Nota") ?: continue // Reemplaza "Nota" con el nombre del campo de la nota en tu base de datos
                    val id = document.getString("id") ?: ""
                    val usuario = document.getString("Usuario") ?: ""
                    val fecha = document.getString("fecha") ?: ""
                    val dataNota = DatosNotas(nota, usuario, fecha, id)
                    listaNotas.add(dataNota)
                }

                // Mostrar las notas en un diálogo
                if (listaNotas.isEmpty()) {
                    Toast.makeText(context, "No hay notas para esta fecha", Toast.LENGTH_SHORT).show()

                } else {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Notas para el ${selectedDate.get(Calendar.DAY_OF_MONTH)}/${selectedDate.get(Calendar.MONTH) + 1}/${selectedDate.get(Calendar.YEAR)}")

                    // Crear un LinearLayout vertical para agregar los TextView y los botones de cada nota
                    val linearLayout = LinearLayout(context)
                    linearLayout.orientation = LinearLayout.VERTICAL

                    // Recorrer la lista de notas y agregar los TextView y los botones de cada nota al LinearLayout
                    for (nota in listaNotas) {
                        val horizontalLayout = LinearLayout(context)
                        val textView = TextView(context)
                        textView.text = nota.Nota

                        textView.setPadding(60,0,0,0)

                        val deleteButton = Button(context)
                        deleteButton.text = "Eliminar"
                        deleteButton.setTextColor(Color.WHITE)
                        deleteButton.setOnClickListener {
                            showDeleteConfirmationDialog(context, nota.id) // Pasar el id de la nota en lugar del objeto completo
                            linearLayout.removeView(horizontalLayout) // Quitar el LinearLayout correspondiente a la nota eliminada
                            listaNotas.remove(nota)
                        }

                        horizontalLayout.orientation = LinearLayout.HORIZONTAL
                        horizontalLayout.addView(textView)
                        horizontalLayout.addView(deleteButton)

                        linearLayout.addView(horizontalLayout)
                    }

                    builder.setView(linearLayout)

                    builder.setPositiveButton("Ok") { dialog, which ->
                        dialog.cancel()
                    }

                    builder.show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al obtener notas", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para mostrar el diálogo de confirmación de eliminación de nota
    private fun showDeleteConfirmationDialog(context: Context, id: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar nota")
        builder.setMessage("¿Está seguro de que desea eliminar esta nota?")

        builder.setPositiveButton("Sí") { dialog, which ->
            // Eliminar el documento correspondiente
            db.collection("Eventos").document(id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al eliminar nota", Toast.LENGTH_SHORT).show()
                }
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}