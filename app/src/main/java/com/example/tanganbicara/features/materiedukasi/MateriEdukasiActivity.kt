package com.example.tanganbicara.features.materiedukasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanganbicara.R
import com.example.tanganbicara.features.materiedukasi.data.AppDatabase
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiDao
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiEntity
import com.example.tanganbicara.features.materiedukasi.data.adapter.MateriEdukasiAdapter
import kotlinx.coroutines.launch
import android.widget.ImageButton
import android.util.Log

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

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerMateri)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter dengan data kosong
        adapter = MateriEdukasiAdapter(emptyList())
        recyclerView.adapter = adapter

        // Init Database
        val db = AppDatabase.getDatabase(this)
        materiDao = db.materiEdukasiDao()

        // Observe data dari DB dan update RecyclerView
        materiDao.getAllMateri().observe(this) { listMateri ->
            Log.d("MateriEdukasiActivity", "Jumlah materi yang diterima: ${listMateri.size}")
            val sortedList = listMateri.sortedBy { it.judul }
            adapter.updateData(sortedList) // Update data di adapter
        }


        // Insert dummy data jika kosong
        lifecycleScope.launch {
            try {
                // Insert data dummy hanya jika tidak ada materi di DB
                if (materiDao.getAllMateri().value.isNullOrEmpty()) {
                    materiDao.insertAll(
                        listOf(
                            MateriEdukasiEntity(
                                judul = "Bahasa Isyarat Dasar",
                                jumlahMateri = 5,
                                progress = 40,
                                gambarResId = R.drawable.ic_launcher_foreground
                            ),
                            MateriEdukasiEntity(
                                judul = "Bahasa Isyarat Lanjutan",
                                jumlahMateri = 7,
                                progress = 60,
                                gambarResId = R.drawable.ic_launcher_foreground
                            )
                        )
                    )
                    Log.d("MateriEdukasiActivity", "Data dummy berhasil ditambahkan.")
                }
            } catch (e: Exception) {
                Log.e("MateriEdukasiActivity", "Error saat menambah data dummy", e)
            }
        }

        // Tombol back
        val btnBack = findViewById<ImageButton>(R.id.btn_backEdukasi)
        btnBack.setOnClickListener {
            finish()
        }
    }
}
