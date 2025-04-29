package com.example.tanganbicara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton

class MateriEdukasi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_edukasi) // sesuaikan nama layout

        // Temukan tombol setelah setContentView dipanggil
        val backButton = findViewById<ImageButton>(R.id.btn_backEdukasi)

        // Hapus tint default
        backButton.imageTintList = null

        // Set click listener untuk tombol back
        backButton.setOnClickListener {
            // Balik ke halaman home (bisa MainActivity atau yang lain)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Tutup activity sekarang biar gak numpuk
        }
    }
}
