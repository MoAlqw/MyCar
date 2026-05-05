package com.example.mycar.fragments.vinscanner

class VinTextParser {

    fun extract(rawText: String): String? {
        val candidates = rawText
            .uppercase()
            .replace("\n", " ")
            .split(" ", ":", ";", ",", ".", "|", "/", "\\", "-", "_")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        for (candidate in candidates) {
            val cleaned = candidate
                .replace(" ", "")
                .replace("O", "0")
                .replace("I", "1")

            if (cleaned.length == 17 && VIN_REGEX.matches(cleaned)) {
                return cleaned
            }
        }
        return null
    }

    companion object {
        private val VIN_REGEX = Regex("^[A-HJ-NPR-Z0-9]{17}$")
    }
}