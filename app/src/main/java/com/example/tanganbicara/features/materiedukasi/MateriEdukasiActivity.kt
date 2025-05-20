package com.example.tanganbicara.features.materiedukasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanganbicara.R
import com.example.tanganbicara.features.materiedukasi.data.AppDatabase
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiEntity
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiDao
import com.example.tanganbicara.features.materiedukasi.data.MateriJson
import com.example.tanganbicara.features.materiedukasi.data.adapter.MateriEdukasiAdapter
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MateriEdukasiActivity : AppCompatActivity() {

    private lateinit var materiDao: MateriEdukasiDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MateriEdukasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_edukasi)

        // Status bar putih
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        recyclerView = findViewById(R.id.recyclerMateri)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MateriEdukasiAdapter { materi ->
            val intent = Intent(this, DetailMateriActivity::class.java)
            intent.putExtra("MATERI_ID", materi.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Init Database dan DAO
        val db = AppDatabase.getDatabase(applicationContext)
        materiDao = db.materiEdukasiDao()

        lifecycleScope.launch {
            // Tunggu load dan insert data JSON ke DB selesai dulu
            loadJsonAndInsertToDb()

            // Baru observe data dari DB
            materiDao.getAllMateri().observe(this@MateriEdukasiActivity) { listMateri ->
                Log.d("MateriEdukasiActivity", "Jumlah materi yang diterima: ${listMateri.size}")
                val sortedList = listMateri.sortedBy { it.judul }
                adapter.submitList(sortedList)
            }
        }

        // Tombol back
        findViewById<ImageButton>(R.id.btn_backEdukasi).setOnClickListener {
            finish()
        }
    }

    private suspend fun loadJsonAndInsertToDb() {
        val count = materiDao.getCount()
        Log.d("DEBUG_INSERT", "Jumlah data di DB: $count")
        if (count == 0) {
            Log.d("DEBUG_INSERT", "Memulai load JSON")

            materiDao.deleteAll()

            val jsonString = assets.open("materi.json")
                .bufferedReader()
                .use { it.readText() }

            val listJson = Gson().fromJson(jsonString, Array<MateriJson>::class.java).toList()

            val listEntity = listJson.map {
                val resId = resources.getIdentifier(it.gambarResId, "drawable", packageName)
                    .takeIf { id -> id != 0 } ?: R.drawable.ic_launcher_foreground

                MateriEdukasiEntity(
                    id = 0,
                    judul = it.judul,
                    jumlahMateri = it.jumlahMateri,
                    progress = it.progress,
                    gambarResId = resId
                )
            }

            materiDao.insertAll(listEntity)
            Log.d("DEBUG_INSERT", "Insert JSON selesai")
        } else {
            Log.d("DEBUG_INSERT", "DB sudah berisi data, tidak insert ulang")
        }
    }
}
