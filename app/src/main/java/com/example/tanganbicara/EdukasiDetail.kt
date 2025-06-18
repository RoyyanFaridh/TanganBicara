package com.example.tanganbicara

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EdukasiDetail : AppCompatActivity() {

    private lateinit var txtJudulMateri: TextView
    private lateinit var txtIsiMateri: TextView
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edukasi_materi) // pastikan ini layout-nya sesuai

        txtJudulMateri = findViewById(R.id.txtJudulMateri)
        txtIsiMateri = findViewById(R.id.txtIsiMateri)
        btnBack = findViewById(R.id.btn_backEdukasi)

        btnBack.setOnClickListener {
            finish()
        }

        // Ambil data dari intent
        val judulSubbab = intent.getStringExtra("judulSubbab")
        val kontenJson = intent.getStringExtra("konten")

        val kontenList: List<Konten> = Gson().fromJson(
            kontenJson,
            object : TypeToken<List<Konten>>() {}.type
        )

        // Set judul
        txtJudulMateri.text = judulSubbab

        // Gabungkan isi konten tipe teks
        val isiMateri = StringBuilder()
        for (konten in kontenList) {
            if (konten.tipe == "teks") {
                isiMateri.append(konten.isi).append("\n\n")
            }
            // Gambar bisa kamu tangani nanti di versi RecyclerView kalau ingin fleksibel
        }

        txtIsiMateri.text = isiMateri.toString().trim()
    }
}
