package com.example.tanganbicara.features.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tanganbicara.databinding.FragmentMainBinding
import com.example.tanganbicara.features.materiedukasi.MateriEdukasiActivity
import com.example.tanganbicara.features.penerjemahanisyarat.PenerjemahanIsyarat

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup padding system bars menggunakan ViewCompat
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainFragment) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set klik listener CardView via binding
        binding.PenerjemahanIsyarat.setOnClickListener {
            val intent = Intent(requireContext(), PenerjemahanIsyarat::class.java)
            startActivity(intent)
        }

        binding.MateriEdukasi.setOnClickListener {
            val intent = Intent(requireContext(), MateriEdukasiActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}