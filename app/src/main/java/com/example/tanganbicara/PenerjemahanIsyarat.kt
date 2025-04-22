package com.example.tanganbicara

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import android.content.pm.PackageManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat

//Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraControl
import androidx.cardview.widget.CardView


class PenerjemahanIsyarat : AppCompatActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private var currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var camera: Camera? = null // Gunakan nullable untuk kamera
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_penerjemahan_isyarat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Meminta izin kamera
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Jika izin sudah diberikan, setup kamera
            setupCamera()
        }

        // Tombol untuk mengganti kamera
        findViewById<CardView>(R.id.switchCameraButton).setOnClickListener {
            toggleCamera()
        }
    }

    private fun setupCamera() {
        val previewView = findViewById<PreviewView>(R.id.previewView)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get() // <== ini yang penting

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            cameraProvider.bindToLifecycle(this, currentCameraSelector, preview) // gunakan currentCameraSelector
        }, ContextCompat.getMainExecutor(this))
    }


    private fun bindCameraUseCases() {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(findViewById<PreviewView>(R.id.previewView).surfaceProvider)
        }

        // Bind kamera berdasarkan CameraSelector yang sedang aktif
        try {
            cameraProvider.unbindAll() // Penting: unbind sebelum bind ulang
            cameraProvider.bindToLifecycle(this, currentCameraSelector, preview)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleCamera() {
        // Ganti antara kamera depan dan belakang
        currentCameraSelector = if (currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        // Tambahkan pengecekan apakah cameraProvider sudah siap
        if (::cameraProvider.isInitialized) {
            bindCameraUseCases()
        } else {
            Toast.makeText(this, "Kamera belum siap", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Izin diberikan, lakukan pengaturan kamera
                    setupCamera()
                } else {
                    // Izin ditolak, beri tahu pengguna
                    Toast.makeText(this, "Izin Kamera Diperlukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}