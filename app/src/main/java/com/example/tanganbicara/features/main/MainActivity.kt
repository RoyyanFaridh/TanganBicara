package com.example.tanganbicara.features.main

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.WindowInsetsCompat
import androidx.cardview.widget.CardView
import com.example.tanganbicara.features.penerjemahanisyarat.PenerjemahanIsyarat
import com.example.tanganbicara.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tanganbicara.features.materiedukasi.MateriEdukasiFragment
import com.example.tanganbicara.features.penerjemahanisyarat.PenerjemahanIsyaratFragment
import com.example.tanganbicara.features.tekskevideo.TeksKeVideoFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment())
            .commit()

        bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> MainFragment()
                R.id.nav_search -> TeksKeVideoFragment()
                R.id.nav_favorite -> PenerjemahanIsyaratFragment()
                R.id.nav_profile -> MateriEdukasiFragment()
                else -> MainFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, MainFragment())
                .commit()
        }
    }
}
