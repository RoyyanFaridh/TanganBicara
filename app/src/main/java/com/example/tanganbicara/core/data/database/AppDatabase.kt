package com.example.tanganbicara.features.materiedukasi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Tentukan entitas yang akan digunakan dalam database, serta versi database
@Database(entities = [MateriEdukasiEntity::class], version = 2, exportSchema = false) // versi naik
abstract class AppDatabase : RoomDatabase() {

    abstract fun materiEdukasiDao(): MateriEdukasiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "materi_edukasi_database"
                )
                    .fallbackToDestructiveMigration() // reset DB kalau versi berubah
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
