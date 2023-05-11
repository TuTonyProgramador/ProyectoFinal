package com.example.proyectofinal

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.proyectofinal.login.MainActivity
import com.example.proyectofinal.databinding.ActivityPajarosBinding
import com.example.proyectofinal.menu.*
import com.google.firebase.auth.FirebaseAuth


class PajarosActivity : AppCompatActivity() {
    // Declarar variables privadas y lateinit
    private lateinit var toogle: ActionBarDrawerToggle
    lateinit var binding: ActivityPajarosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout de la actividad
        binding = ActivityPajarosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toogle = ActionBarDrawerToggle(this, binding.drawer, R.string.open_drawer, R.string.close_drawer)
        binding.drawer.addDrawerListener(toogle)
        toogle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Cargar el correo en la cabecera del menu
        val nav = binding.navView.getHeaderView(0)
        val email = nav.findViewById<TextView>(R.id.CorreoUsu)
        email.text = intent.getStringExtra("emailUsuario")

        // Establecer un listener al menú de navegación
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.pajaros_registrados -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, HomeFragment())
                        commit()
                    }
                }
                R.id.anadir_nuevo -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, AnadirFragment())
                        commit()
                    }
                }
                R.id.consultar_criadores -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, CriadorFragment())
                        commit()
                    }
                }
                R.id.modificar_ave -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, ModificarFragment())
                        commit()
                    }
                }
                R.id.cerrar_sesion -> {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "Ha cerrado seccion", Toast.LENGTH_SHORT).show()
                    // Volver a la actividad del MainActivity (al inicio de seccion)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
                R.id.modo_Oscuro -> {

                }
                R.id.soporte -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, SoporteFragment())
                        commit()
                    }
                }
            }
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }

        // Modo oscuro

        // Obtener la referencia al botón switch del menú
        val switch = binding.navView.menu.findItem(R.id.modo_Oscuro).actionView as Switch

        // Establecer el estado inicial del switch
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        switch.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        // Agregar un listener al botón switch
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Activar modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Activar modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
