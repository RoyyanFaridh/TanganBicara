package com.example.tanganbicara.features.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.tanganbicara.R
import com.example.tanganbicara.MateriEdukasi
import com.example.tanganbicara.features.penerjemahanisyarat.PenerjemahanIsyarat
import com.example.tanganbicara.features.tekskevideo.TeksKeVideo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Aksi klik pada CardView untuk fitur Teks ke Video
        findViewById<CardView>(R.id.TeksKeVideo)?.setOnClickListener {
            val intent = Intent(this, TeksKeVideo::class.java)
            startActivity(intent)
        }

        // Aksi klik pada CardView untuk fitur Penerjemahan Isyarat
        findViewById<CardView>(R.id.PenerjemahanIsyarat)?.setOnClickListener {
            val intent = Intent(this, PenerjemahanIsyarat::class.java)
            startActivity(intent)
        }

        // Aksi klik pada CardView untuk fitur Materi Edukasi
        findViewById<CardView>(R.id.MateriEdukasi)?.setOnClickListener {
            val intent = Intent(this, MateriEdukasi::class.java)
            startActivity(intent)
        }
    }
}
