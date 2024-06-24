package com.example.renessans7.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.renessans7.R
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class PDFUtil @Inject constructor(private val sharedPref: SharedPref) {
    suspend fun loadPdfToViewer(url: String, pdfView: PDFView, onError: (String) -> Unit) {
        try {
            val input: InputStream = withContext(Dispatchers.IO) {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.setRequestProperty(
                    Constants.AUTHORIZATION,
                    "${Constants.BEARER}${sharedPref.token}"
                ) // Add token in header (more secure)
                connection.inputStream
            }

            pdfView.fromStream(input)
                .enableSwipe(true) // allows to block changing pages using swipe
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load()
        } catch (e: Exception) {
            onError(sharedPref.context.getString(R.string.str_error_load))
        }
    }
}