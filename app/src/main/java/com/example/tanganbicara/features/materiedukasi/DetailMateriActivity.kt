package com.example.tanganbicara.features.materiedukasi

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tanganbicara.R
import com.example.tanganbicara.features.materiedukasi.data.AppDatabase
import com.example.tanganbicara.features.materiedukasi.data.model.DetailMateri
import com.example.tanganbicara.features.materiedukasi.data.model.SubMateri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMateriActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_materi)

        val materiId = intent.getIntExtra("MATERI_ID", -1)

        if (materiId != -1) {
            val db = AppDatabase.getDatabase(this)
            val dao = db.materiEdukasiDao()

            // Observe LiveData tidak dalam coroutine
            dao.getMateriById(materiId).observe(this) { materi ->
                if (materi != null) {
                    findViewById<TextView>(R.id.textJudulDetail).text = materi.judul
                    findViewById<TextView>(R.id.textJumlahDetail).text = "${materi.jumlahMateri} Materi"
                    findViewById<ProgressBar>(R.id.progressDetail).progress = materi.progress
                    findViewById<ImageView>(R.id.imageDetail).setImageResource(materi.gambarResId)
                }
            }

            // Load JSON di coroutine
            lifecycleScope.launch {
                val detail = loadDetailMateriFromJson(materiId)
                val kontenTextView = findViewById<TextView>(R.id.textKontenDetail)
                kontenTextView.text = detail?.isi?.joinToString("\n\n") { subMateri ->
                    "• ${subMateri.judulSub}\n${subMateri.konten}"
                } ?: "Konten tidak ditemukan."
            }
        }
    }

    private suspend fun loadDetailMateriFromJson(materiId: Int): DetailMateri? {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = assets.open("detail_materi.json")
                    .bufferedReader().use { it.readText() }

                val list = Gson().fromJson(jsonString, Array<DetailMateri>::class.java).toList()
                list.find { it.materiId == materiId }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
