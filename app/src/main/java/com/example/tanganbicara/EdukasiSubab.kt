package com.example.tanganbicara

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class EdukasiSubab : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edukasi_subab)

        // Temukan tombol setelah setContentView dipanggil
        val backButton = findViewById<ImageButton>(R.id.btn_backEdukasi)
        backButton.imageTintList = null
        backButton.setOnClickListener {
            val intent = Intent(this, MateriEdukasi::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }

        val recyclerView = findViewById<RecyclerView>(R.id.rvSubab)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val materiJson = intent.getStringExtra("materi")
        val materi = Gson().fromJson(materiJson, Materi::class.java)

        val txtJudul = findViewById<TextView>(R.id.txtJudul)
        val txtDeskripsi = findViewById<TextView>(R.id.txtDeskripsi)
//        val txtBreadcrumb = findViewById<TextView>(R.id.txtBreadcrumb)

        txtJudul.text = materi.judul
        txtDeskripsi.text = materi.deskripsi
//        txtBreadcrumb.text = "Edukasi > ${materi.judul}"


        val adapter = SubabAdapter(materi.subbab) // Asumsinya `subbab` adalah List<Subab>
        recyclerView.adapter = adapter

    }
}