package com.example.tanganbicara

import android.Manifest
import android.util.Log
import android.os.Bundle
import android.widget.Toast
import android.widget.FrameLayout
import android.content.pm.PackageManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.content.Intent
import android.view.WindowManager
import android.provider.Settings
import android.widget.ImageButton

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
    private var flashEnabled = false   // Untuk melacak status flash
    private var initialBrightness: Float = -1f


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

        val flashButton = findViewById<CardView>(R.id.flashCameraButton)
        flashButton.setOnClickListener {
            toggleFlash()
        }

        val backButton = findViewById<ImageButton>(R.id.btn_backPenerjemahanIsyarat)
        backButton.imageTintList = null // Menghapus warna default dari gambarbutton.imageTintList = null

        backButton.setOnClickListener {
            // Balik ke halaman home (bisa MainActivity atau yang lain)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Tutup activity sekarang biar ga numpuk
        }


    }

    private fun setupCamera() {
        val previewView = findViewById<PreviewView>(R.id.previewView)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            try {
                cameraProvider?.unbindAll() // Unbind dulu biar gak bentrok
                camera = cameraProvider?.bindToLifecycle(this, currentCameraSelector, preview)
            } catch (e: Exception) {
                e.printStackTrace()
            }

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
        val wasFront = currentCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA

        // Ganti selector
        currentCameraSelector = if (wasFront) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        // Matikan flash jika sebelumnya kamera depan
        if (wasFront && flashEnabled) {
            simulateScreenFlash(false)
            flashEnabled = false
        }

        // Matikan torch jika sebelumnya kamera belakang dan torch menyala
        if (!wasFront && flashEnabled) {
            camera?.cameraControl?.enableTorch(false)
            flashEnabled = false
        }

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

    // Fungsi untuk mengatur brightness
    private fun setScreenBrightness(brightness: Float) {
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
    }

    // Fungsi untuk mendapatkan brightness awal
    private fun getInitialBrightness(): Float {
        // Ambil nilai brightness awal dari settings sistem
        return Settings.System.getFloat(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0f)
    }

    private fun toggleFlash() {
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        val isFront = currentCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA

        flashEnabled = !flashEnabled

        if (isFront) {
            if (flashEnabled) {
                // Mencatat brightness saat ini sebelum mengubahnya
                initialBrightness = getInitialBrightness()
                setScreenBrightness(1.0f)  // Set ke 100% untuk flash
                simulateScreenFlash(true)
            } else {
                // Mengembalikan brightness ke nilai awal setelah matikan flash
                setScreenBrightness(initialBrightness)
                simulateScreenFlash(false)
            }
        } else if (hasFlash) {
            camera?.cameraControl?.enableTorch(flashEnabled)
        } else {
            Toast.makeText(this, "Flash tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun simulateScreenFlash(enable: Boolean) {
        val screenFlashOverlay = findViewById<View>(R.id.screenFlashOverlay)

        runOnUiThread {
            if (enable) {
                Log.d("ScreenFlash", "Enabling screen flash")
                setScreenBrightness(1f) // brightness 100%
                screenFlashOverlay.apply {
                    alpha = 1f
                    visibility = View.VISIBLE
                    bringToFront()
                }
            } else {
                Log.d("ScreenFlash", "Disabling screen flash")
                setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE) // kembali ke default user
                screenFlashOverlay.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        screenFlashOverlay.visibility = View.GONE
                        Log.d("ScreenFlash", "Screen flash disabled")
                    }
                    .start()
            }
        }
    }

}