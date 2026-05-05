package com.example.mycar.fragments.vehicleinspection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mycar.databinding.FragmentInspectionPhotoPreviewBinding
import com.example.mycar.fragments.BaseFragment
import com.example.mycar.model.inspection.InspectionCaptureUiState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class InspectionPhotoPreviewFragment : BaseFragment<FragmentInspectionPhotoPreviewBinding>(
    FragmentInspectionPhotoPreviewBinding::inflate
) {

    private val viewModel: InspectionCaptureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val router: InspectionFlowFragment
        get() = requireParentFragment() as InspectionFlowFragment

    private var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeState()
        setupClicks()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { render(it) }
            }
        }
    }

    private fun render(state: InspectionCaptureUiState) = with(binding) {
        ivPreview.setImageURI(state.currentStep.photoUri)

        if (state.isUploading && !isLoading) {
            startLoading()
        }
    }

    private fun startLoading() {
        if (isLoading) return
        isLoading = true

        binding.clMainContent.visibility = View.GONE
        binding.loadingContainer.visibility = View.VISIBLE
        binding.loadingContainer.alpha = 1f

        binding.lottieLoading.playAnimation()
    }

    private fun stopLoading() {
        if (!isLoading) return
        isLoading = false

        binding.lottieLoading.cancelAnimation()

        binding.loadingContainer.animate()
            .alpha(0f)
            .setDuration(150)
            .start()
    }

    private fun setupClicks() = with(binding) {

        btnBack.setOnClickListener {
            viewModel.retakeCurrentPhoto()
            router.goBack()
        }

        btnRetake.setOnClickListener {
            viewModel.retakeCurrentPhoto()
            router.goBack()
        }

        btnConfirm.setOnClickListener {
            if (isLoading) return@setOnClickListener

            if (viewModel.isLastStep()) {
                startLoading()

                viewModel.uploadInspection(
                    onSuccess = {
                        stopLoading()
                        router.finishFlow(it)
                    },
                    onError = {
                        stopLoading()
                        Snackbar.make(
                            binding.root,
                            it ?: "Something went wrong..",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                )
            } else {
                viewModel.goToNextStep()
                router.goBack()
            }
        }
    }

    override fun onStop() {
        binding.lottieLoading.cancelAnimation()
        super.onStop()
    }
}