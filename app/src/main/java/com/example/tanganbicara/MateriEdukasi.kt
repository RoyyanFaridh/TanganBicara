package com.example.tanganbicara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.example.tanganbicara.features.main.MainActivity

class MateriEdukasi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_edukasi) // sesuaikan nama layout

        // Temukan tombol setelah setContentView dipanggil
        val backButton = findViewById<ImageButton>(R.id.btn_backEdukasi)
        backButton.imageTintList = null
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }

        val cardEdukasiSubab = findViewById<CardView>(R.id.EdukasiSubab)
        cardEdukasiSubab.setOnClickListener {
            val intent = Intent(this, EdukasiSubab::class.java)
            startActivity(intent)
        }
    }
}
