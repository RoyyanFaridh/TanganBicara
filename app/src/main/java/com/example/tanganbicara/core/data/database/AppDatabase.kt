package com.example.tanganbicara.features.materiedukasi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Tentukan entitas yang akan digunakan dalam database, serta versi database
@Database(entities = [MateriEdukasiEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Tentukan DAO yang akan digunakan untuk mengakses database
    abstract fun materiEdukasiDao(): MateriEdukasiDao

    // Singleton pattern untuk mendapatkan instance database
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Fungsi untuk mendapatkan instance database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "materi_edukasi_database" // Nama database
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
