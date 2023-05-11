package com.example.proyectofinal.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.R


class SoporteFragment : Fragment(R.layout.fragment_soporte) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño de diseño para este fragmento.
        return inflater.inflate(R.layout.fragment_soporte, container, false)
    }


}