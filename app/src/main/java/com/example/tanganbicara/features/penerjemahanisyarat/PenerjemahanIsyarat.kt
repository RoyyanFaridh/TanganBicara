package com.example.tanganbicara.features.penerjemahanisyarat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tanganbicara.R
import com.example.tanganbicara.features.main.MainActivity
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.TaskResult
import androidx.camera.core.Camera
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

class PenerjemahanIsyarat : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    private var currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var camera: Camera? = null
    private lateinit var cameraProvider: ProcessCameraProvider
    private var flashEnabled = false
    private var initialBrightness: Float = -1f
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var gestureRecognizer: GestureRecognizer
    private val executor = Executors.newSingleThreadExecutor()
    private val gestureBuffer = StringBuilder()
    private var lastGesture: String? = null
    private var lastGestureTimestamp = 0L
    private val gestureTimeout = 1500L
    private val detectDelay = 2000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_penerjemahan_isyarat)

        setupEdgeInsets()
        checkCameraPermission()
        setupButtonListeners()
    }

    private fun setupEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startCameraAndRecognizer()
        }
    }

    private fun startCameraAndRecognizer() {
        setupCamera()
        setupGestureRecognizer()
    }

    private fun setupButtonListeners() {
        findViewById<CardView>(R.id.switchCameraButton).setOnClickListener { toggleCamera() }
        findViewById<CardView>(R.id.flashCameraButton).setOnClickListener { toggleFlash() }
        findViewById<ImageButton>(R.id.btn_backPenerjemahanIsyarat).apply {
            imageTintList = null
            setOnClickListener {
                val intent = Intent(this@PenerjemahanIsyarat, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
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

            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(executor) { imageProxy ->
                        analyzeImage(imageProxy)
                    }
                }

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, currentCameraSelector, preview, imageAnalyzer)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun setupGestureRecognizer() {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("gesture_recognizer.task")
            .build()

        val options = GestureRecognizer.GestureRecognizerOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setResultListener { result: GestureRecognizerResult, _ ->
                val gestureCategory = result.gestures()
                    .firstOrNull()?.firstOrNull()

                val gestureText = if (gestureCategory != null && gestureCategory.score() > 0.5) {
                    gestureCategory.categoryName()
                } else {
                    null
                }

                runOnUiThread {
                    val currentTime = System.currentTimeMillis()

                    if (gestureText == null || gestureText == "Tidak dikenali") {
                        if (currentTime - lastGestureTimestamp > gestureTimeout) {
                            gestureBuffer.clear()
                            findViewById<TextView>(R.id.txt_terjemahan).text = ""
                            lastGesture = null
                        }
                    } else {
                        // Cek apakah delay sudah terpenuhi sebelum menambahkan gesture baru
                        if (gestureText != lastGesture && currentTime - lastGestureTimestamp >= detectDelay) {
                            if (gestureBuffer.isNotEmpty()) {
                                gestureBuffer.append(" ")
                            }
                            gestureBuffer.append(gestureText)
                            findViewById<TextView>(R.id.txt_terjemahan).text = gestureBuffer.toString()
                            lastGesture = gestureText
                            lastGestureTimestamp = currentTime
                        } else if (gestureText == lastGesture) {
                            // Update timestamp supaya buffer tidak di-clear
                            lastGestureTimestamp = currentTime
                        }
                        // Jika delay belum terpenuhi dan gesture berbeda, abaikan dulu (tidak ditambahkan)
                    }
                }
            }
            .build()
        gestureRecognizer = GestureRecognizer.createFromOptions(this, options)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun analyzeImage(imageProxy: ImageProxy) {
        val bitmap = imageProxyToBitmap(imageProxy)
        val mpImage: MPImage = BitmapImageBuilder(bitmap).build()

        // Gunakan recognizeAsync karena RunningMode.LIVE_STREAM
        gestureRecognizer.recognizeAsync(mpImage, imageProxy.imageInfo.timestamp)

        imageProxy.close()
    }

    private fun toggleCamera() {
        val wasFront = currentCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
        currentCameraSelector = if (wasFront) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

        if (wasFront && flashEnabled) {
            simulateScreenFlash(false)
            flashEnabled = false
        } else if (!wasFront && flashEnabled) {
            camera?.cameraControl?.enableTorch(false)
            flashEnabled = false
        }

        if (::cameraProvider.isInitialized) {
            setupCamera()
        } else {
            Toast.makeText(this, "Kamera belum siap", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraAndRecognizer()
        } else {
            Toast.makeText(this, "Izin Kamera Diperlukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFlash() {
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        val isFront = currentCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA

        flashEnabled = !flashEnabled

        when {
            isFront && flashEnabled -> {
                initialBrightness = getInitialBrightness()
                setScreenBrightness(1.0f)
                simulateScreenFlash(true)
            }
            isFront && !flashEnabled -> {
                setScreenBrightness(initialBrightness)
                simulateScreenFlash(false)
            }
            hasFlash -> {
                camera?.cameraControl?.enableTorch(flashEnabled)
            }
            else -> {
                Toast.makeText(this, "Flash tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun simulateScreenFlash(enable: Boolean) {
        val screenFlashOverlay = findViewById<View>(R.id.screenFlashOverlay)
        runOnUiThread {
            if (enable) {
                screenFlashOverlay.apply {
                    alpha = 1f
                    visibility = View.VISIBLE
                    bringToFront()
                }
            } else {
                screenFlashOverlay.animate().alpha(0f).setDuration(300).withEndAction {
                    screenFlashOverlay.visibility = View.GONE
                }.start()
            }
        }
    }

    private fun setScreenBrightness(brightness: Float) {
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
    }

    private fun getInitialBrightness(): Float {
        return Settings.System.getFloat(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0f)
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
        val jpegBytes = out.toByteArray()

        val bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)

        val matrix = Matrix()
        // Rotasi 90 derajat (sesuaikan dengan orientasi kamera)
        matrix.postRotate(90f)
        // Jika kamera depan, flip horizontal
        if (currentCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            matrix.preScale(-1f, 1f)
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
