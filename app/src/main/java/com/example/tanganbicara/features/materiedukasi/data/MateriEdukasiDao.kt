package com.example.tanganbicara.features.materiedukasi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MateriEdukasiDao {

    // Menambahkan materi edukasi baru ke dalam database
    @Insert
    suspend fun insert(materiEdukasi: MateriEdukasi)

    // Menambahkan beberapa materi edukasi sekaligus
    @Insert
    suspend fun insertAll(materiList: List<MateriEdukasi>)

    // Mengambil semua materi edukasi dari database
    @Query("SELECT * FROM materi_edukasi")
    fun getAllMateri(): LiveData<List<MateriEdukasi>>

    // Mengambil materi edukasi berdasarkan ID
    @Query("SELECT * FROM materi_edukasi WHERE id = :id")
    fun getMateriById(id: Int): LiveData<MateriEdukasi>

    // Mengupdate data materi edukasi
    @Update
    suspend fun update(materiEdukasi: MateriEdukasi)

    // Menghapus materi edukasi berdasarkan ID
    @Query("DELETE FROM materi_edukasi WHERE id = :id")
    suspend fun deleteById(id: Int)

    // Menghapus semua materi edukasi
    @Query("DELETE FROM materi_edukasi")
    suspend fun deleteAll()
}
