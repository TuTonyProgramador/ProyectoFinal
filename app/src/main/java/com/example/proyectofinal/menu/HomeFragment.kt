package com.example.proyectofinal.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.data.DatosAve
import com.example.proyectofinal.adapter.ProyectoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // Declara una variable opcional para el RecyclerView y una lista de DatosAve
    private var recycler: RecyclerView? = null
    private var listaAves = ArrayList<DatosAve>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Infla la vista del fragmento
        var vista: View = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializa la lista de DatosAve y el RecyclerView
        listaAves = ArrayList()
        recycler = vista.findViewById(R.id.Recycler)
        recycler?.layoutManager = LinearLayoutManager(context)

        // Llamada al metodo para cargar los datos de los canarios
        cargarDatos()

        // Asigna un adapter a la lista de DatosAve para mostrarla en el RecyclerView
        recycler?.adapter = ProyectoAdapter(listaAves)

        // Devuelve la vista del fragmento
        return vista
    }

    // Método que carga los datos de los canarios en la lista de DatosAve
    fun cargarDatos() {
        // Obtiene una instancia de la base de datos Firestore y el email del usuario actual
        val db = FirebaseFirestore.getInstance()
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        // Filtrar documentos por el campo "usuario" que tenga el valor del email del usuario actual
        db.collection("Canarios")
            .whereEqualTo("Usuario", currentUserEmail)
            .get()
            .addOnSuccessListener { cargar ->
                // Recorre los documentos y los guarda en la lista de DatosAve
                for (canarios in cargar) {
                    var canario = canarios.toObject(DatosAve::class.java)
                    listaAves.add(canario)
                }
                // Crea un adapter y lo asigna a la lista de DatosAve para mostrarla en el RecyclerView
                var adapter = ProyectoAdapter(listaAves)
                recycler?.adapter = adapter
            }
            .addOnFailureListener {
                Log.d("Cargar", "Error en la obtención del canario")
            }
    }
}