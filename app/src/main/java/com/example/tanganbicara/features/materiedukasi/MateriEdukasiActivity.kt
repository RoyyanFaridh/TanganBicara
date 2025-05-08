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
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasi
import com.example.tanganbicara.features.materiedukasi.data.MateriEdukasiDao
import com.example.tanganbicara.features.materiedukasi.data.adapter.MateriEdukasiAdapter
import kotlinx.coroutines.launch
import android.widget.ImageButton

class MateriEdukasiActivity : AppCompatActivity() {

    private lateinit var materiDao: MateriEdukasiDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MateriEdukasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_edukasi)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerMateri)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Room database
        val db = AppDatabase.getDatabase(this)
        materiDao = db.materiEdukasiDao()



        // Observe LiveData to get updated list of MateriEdukasi
        materiDao.getAllMateri().observe(this) { listMateri ->
            adapter = MateriEdukasiAdapter(listMateri)
            recyclerView.adapter = adapter
        }

        // Contoh menambah data dummy
        lifecycleScope.launch {
            materiDao.insert(
                MateriEdukasi(
                    judul = "Bahasa Isyarat Dasar",
                    jumlahMateri = 5,
                    progress = 40,
                    gambarResId = R.drawable.ic_launcher_foreground
                )
            )
        }

        // Tombol back
        val btnBack = findViewById<ImageButton>(R.id.btn_backEdukasi)
        btnBack.setOnClickListener {
            finish() // Menutup activity dan kembali ke halaman sebelumnya
        }

    }
}
