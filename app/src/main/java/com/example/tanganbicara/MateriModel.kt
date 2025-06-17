package com.example.tanganbicara

data class Materi(
    val judul: String,
    val deskripsi: String,
    val subbab: List<Subbab>,
    val progress: Int
)

data class Subbab(
    val judul: String,
    val konten: List<Konten>
)

data class Konten(
    val tipe: String,   // teks atau gambar
    val isi: String
)
