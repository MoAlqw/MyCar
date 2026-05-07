package com.example.mycar.fragments.vehicleinspection

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.inspection.instruction
import com.example.domain.model.inspection.title
import com.example.mycar.databinding.FragmentInspectionCameraBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.model.inspection.InspectionCaptureUiState
import com.example.mycar.model.inspection.toOverlay
import kotlinx.coroutines.launch
import java.io.File

class InspectionCameraFragment : BaseFragment<FragmentInspectionCameraBinding>(
    FragmentInspectionCameraBinding::inflate
) {

    private val viewModel: InspectionCaptureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val router: InspectionFlowFragment
        get() = requireParentFragment() as InspectionFlowFragment

    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var imageCapture: ImageCapture

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeState()
        setupClicks()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermission.launch(Manifest.permission.CAMERA)
        }

        binding.ivOverlaySilhouette.animate()
            .alpha(0.4f)
            .setDuration(800)
            .withEndAction {
                binding.ivOverlaySilhouette.animate()
                    .alpha(0.7f)
                    .setDuration(800)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider?.unbindAll()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { render(it) }
            }
        }
    }

    private fun render(state: InspectionCaptureUiState) = with(binding) {
        tvTitle.text = state.currentSide.title()
        tvStep.text = state.progressText
        tvInstruction.text = state.currentSide.instruction()
        applyOverlay(state)
    }

    private fun applyOverlay(side: InspectionCaptureUiState) = with(binding.ivOverlaySilhouette) {
        val config = side.toOverlay()

        setImageResource(config.drawable)
        rotation = config.rotation
        scaleX = config.scaleX
    }

    private fun setupClicks() = with(binding) {
        btnBack.setOnClickListener {
            requireParentFragment().parentFragmentManager.popBackStack()
        }

        btnCapture.setOnClickListener {
            binding.ivOverlaySilhouette.animate()
                .alpha(0f)
                .setDuration(150)
            onCaptureClicked()
        }
    }

    private fun startCamera() {
        val future = ProcessCameraProvider.getInstance(requireContext())

        future.addListener({
            cameraProvider = future.get()
            bindCamera()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCamera() {
        val provider = cameraProvider ?: return

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = binding.previewView.surfaceProvider
        }

        imageCapture = ImageCapture.Builder().build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            provider.unbindAll()

            provider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onCaptureClicked() {
        takePhoto()
    }

    private fun takePhoto() {
        val file = File(
            requireContext().cacheDir,
            "inspection_${System.currentTimeMillis()}.jpg"
        )

        val metadata = ImageCapture.Metadata().apply {
            isReversedHorizontal = false
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file)
            .setMetadata(metadata)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(file)

                    viewModel.saveCapturedPhoto(uri)
                    router.openPreview()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }
}