package com.example.tanganbicara

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import com.google.gson.Gson

class SubabAdapter(private val subbabList: List<Subbab>) :
    RecyclerView.Adapter<SubabAdapter.SubbabViewHolder>() {

    class SubbabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judulTextView: TextView = itemView.findViewById(R.id.textJudul)
        val countTextView: TextView = itemView.findViewById(R.id.textMateriCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubbabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edukasi_subab, parent, false)
        return SubbabViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubbabViewHolder, position: Int) {
        val subbab = subbabList[position]
        holder.judulTextView.text = subbab.judul
        holder.countTextView.text = "Materi ${position + 1}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EdukasiDetail::class.java) // ganti nama kalau berbeda
            intent.putExtra("judulSubbab", subbab.judul)
            intent.putExtra("konten", Gson().toJson(subbab.konten))
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = subbabList.size
}
