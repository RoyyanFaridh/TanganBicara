package com.example.tanganbicara

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Untuk padding sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // INI DITARUH DI LUAR listener insets
        val cardPenerjemahanIsyarat = findViewById<CardView>(R.id.PenerjemahanIsyarat)
        cardPenerjemahanIsyarat.setOnClickListener {
            val intent = Intent(this, PenerjemahanIsyarat::class.java)
            startActivity(intent)
        }
    }
}
