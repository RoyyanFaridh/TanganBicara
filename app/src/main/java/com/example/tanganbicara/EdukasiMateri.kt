//package com.example.tanganbicara
//
//import android.os.Bundle
//import android.widget.ImageButton
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.gson.Gson
//
//class EdukasiMateri : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edukasi_materi)
//
//        // Ambil data dari intent
//        val subbabJson = intent.getStringExtra("subbab")
//        val judulMateri = intent.getStringExtra("judulMateri")
//
//        // Convert JSON string ke objek Subbab
//        val subbab = Gson().fromJson(subbabJson, Subbab::class.java)
//
//        // Temukan view
//        val txtBreadcrumb = findViewById<TextView>(R.id.txtBreadcrumb)
//        val txtJudul = findViewById<TextView>(R.id.txtJudulMateri)
//        val txtIsi = findViewById<TextView>(R.id.txtIsiMateri)
//        val backBtn = findViewById<ImageButton>(R.id.btn_backEdukasi)
//
//        // Isi teks
//        txtBreadcrumb.text = "Edukasi > $judulMateri"
//        txtJudul.text = subbab.judul
////        txtIsi.text = subbab.konten
//
//        backBtn.setOnClickListener {
//            finish()
//        }
//    }
//}
