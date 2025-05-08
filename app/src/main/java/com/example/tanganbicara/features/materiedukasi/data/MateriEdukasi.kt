package com.example.tanganbicara.features.materiedukasi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Menandai kelas ini sebagai entitas yang akan digunakan dalam Room Database
@Entity(tableName = "materi_edukasi")
data class MateriEdukasi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID otomatis
    val judul: String, // Judul materi edukasi
    val jumlahMateri: Int, // Jumlah materi dalam paket edukasi
    val progress: Int, // Progress dari materi edukasi dalam bentuk angka (misal: 40%)
    val gambarResId: Int // Resource ID untuk gambar yang ditampilkan (contoh: gambar ikon)
)
