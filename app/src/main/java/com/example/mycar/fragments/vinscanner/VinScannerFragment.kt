package com.example.mycar.fragments.vinscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.mycar.databinding.FragmentVinScannerBinding
import com.example.mycar.fragments.BaseFragment
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VinScannerFragment : BaseFragment<FragmentVinScannerBinding>(
    FragmentVinScannerBinding::inflate
) {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    private val textRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private val vinTextParser = VinTextParser()

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                parentFragmentManager.popBackStack()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.previewView.scaleType = PreviewView.ScaleType.FILL_CENTER

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        requestCameraPermission()
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> startCamera()

            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .apply {
                    surfaceProvider = binding.previewView.surfaceProvider
                }

            val analyzer = VinImageAnalyzer(
                textRecognizer = textRecognizer,
                vinTextParser = vinTextParser,
                onVinDetected = ::onVinDetected,
                onStatusChanged = ::renderStatus
            )

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(cameraExecutor, analyzer)
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun renderStatus(status: VinImageAnalyzer.Status) {
        if (!isAdded) return

        requireActivity().runOnUiThread {
            binding.tvStatus.text = when (status) {
                VinImageAnalyzer.Status.Scanning -> "Scanning..."
                VinImageAnalyzer.Status.VinDetected -> "VIN detected"
                VinImageAnalyzer.Status.Error -> "Recognition error"
            }
        }
    }

    private fun onVinDetected(vin: String) {
        if (::cameraProviderFuture.isInitialized) {
            cameraProviderFuture.get().unbindAll()
        }

        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT_KEY_VIN to vin)
        )
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        if (::cameraProviderFuture.isInitialized) {
            cameraProviderFuture.get().unbindAll()
        }
        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }
        textRecognizer.close()
        super.onDestroyView()
    }

    companion object {
        const val REQUEST_KEY = "vin_scan_request"
        const val RESULT_KEY_VIN = "vin_result_vin"
    }
}