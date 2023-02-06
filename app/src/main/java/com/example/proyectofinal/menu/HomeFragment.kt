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
import com.example.proyectofinal.adapter.DatosAve
import com.example.proyectofinal.adapter.ProyectoAdapter
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var recycler: RecyclerView? = null
    private var listaAves = ArrayList<DatosAve>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Te carga el recycler
        var vista: View = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializar mi lista
        listaAves = ArrayList()
        recycler = vista.findViewById(R.id.Recycler)
        recycler?.layoutManager = LinearLayoutManager(context)

        // Llamada al metodo para cargar los datos de los canarios
        cargarDatos()

        recycler?.adapter = ProyectoAdapter(listaAves)


        return vista
    }

    fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Canarios")
            .get()
            .addOnSuccessListener { cargar ->
            for (canarios in cargar) {

                var canario = canarios.toObject(DatosAve::class.java)

                listaAves.add(canario)
            }

            var adapter = ProyectoAdapter(listaAves)
            recycler?.adapter = adapter
        }
            .addOnFailureListener{
                Log.d("Cargar", "Error en la obtención del canario")
            }
    }

}