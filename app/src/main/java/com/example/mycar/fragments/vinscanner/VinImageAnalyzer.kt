package com.example.mycar.fragments.vinscanner

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer

class VinImageAnalyzer(
    private val textRecognizer: TextRecognizer,
    private val vinTextParser: VinTextParser,
    private val onVinDetected: (String) -> Unit,
    private val onStatusChanged: (Status) -> Unit
) : ImageAnalysis.Analyzer {

    @Volatile
    private var isProcessing = false

    @Volatile
    private var isVinFound = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing || isVinFound) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        textRecognizer.process(inputImage)
            .addOnSuccessListener { result ->
                val vin = vinTextParser.extract(result.text)
                if (vin != null && !isVinFound) {
                    isVinFound = true
                    onStatusChanged(Status.VinDetected)
                    onVinDetected(vin)
                } else {
                    onStatusChanged(Status.Scanning)
                }
            }
            .addOnFailureListener {
                onStatusChanged(Status.Error)
            }
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }
    }

    enum class Status {
        Scanning,
        VinDetected,
        Error
    }
}