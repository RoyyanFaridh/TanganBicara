package com.example.tanganbicara

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class EdukasiSubab : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edukasi_subab)

        // Inisialisasi tombol kembali
        val backButton = findViewById<ImageButton>(R.id.btn_backEdukasi)
        backButton.imageTintList = null
        backButton.setOnClickListener {
            // Kembali ke halaman sebelumnya
            val intent = Intent(this, MateriEdukasi::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvSubab)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ambil data dari Intent
        val materiJson = intent.getStringExtra("materi")
        if (materiJson != null) {
            val materi = Gson().fromJson(materiJson, Materi::class.java)

            // Tampilkan judul dan deskripsi
            findViewById<TextView>(R.id.txtJudul).text = materi.judul
            findViewById<TextView>(R.id.txtDeskripsi).text = materi.deskripsi

            // Set adapter
            val adapter = SubabAdapter(materi.subbab)
            recyclerView.adapter = adapter
        } else {
            // Fallback jika data null
            findViewById<TextView>(R.id.txtJudul).text = "Materi tidak ditemukan"
        }
    }
}
