package com.example.tanganbicara

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanganbicara.features.main.MainActivity



class MateriEdukasi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_edukasi) // sesuaikan nama layout

        // Temukan tombol setelah setContentView dipanggil
        val backButton = findViewById<ImageButton>(R.id.btn_backEdukasi)
        backButton.imageTintList = null
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }

        val recyclerView = findViewById<RecyclerView>(R.id.rvMateri)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // memuat JSON dari assets dan parsing
        val json = loadJSONFromAsset(this, "materi.json")
        if (json != null) {
            val listType = object : TypeToken<List<Materi>>() {}.type
            val materiList: List<Materi> = Gson().fromJson(json, listType)

            val adapter = MateriAdapter(materiList) { materi ->
                val intent = Intent(this, EdukasiSubab::class.java)
                intent.putExtra("materi", Gson().toJson(materi))
                startActivity(intent)
            }

            recyclerView.adapter = adapter
        }
    }

    // Fungsi membaca JSON dari folder assets
    private fun loadJSONFromAsset(context: Context, filename: String): String? {
        return try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

}
