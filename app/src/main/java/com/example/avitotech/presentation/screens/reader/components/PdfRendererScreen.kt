package com.example.avitotech.presentation.screens.reader.components

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.avitotech.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfRendererScreen(
    file: File,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pdfState = rememberPdfState(file)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PDF: ${file.name}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(
                        onClick = { pdfState.previousPage() },
                        enabled = pdfState.currentPage > 0
                    ) {
                        Text("←")
                    }

                    Text(
                        text = "${pdfState.currentPage + 1}/${pdfState.totalPages}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        onClick = { pdfState.nextPage() },
                        enabled = pdfState.currentPage < pdfState.totalPages - 1
                    ) {
                        Text("→")
                    }

                    IconButton(
                        onClick = { pdfState.zoomOut() },
                        enabled = pdfState.scale > 0.5f
                    ) {
                        Icon(painterResource(R.drawable.ic_zoom_out), contentDescription = "Уменьшить")
                    }

                    IconButton(
                        onClick = { pdfState.zoomIn() },
                        enabled = pdfState.scale < 3f
                    ) {
                        Icon(painterResource(R.drawable.ic_zoom_in), contentDescription = "Увеличить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.DarkGray)
        ) {
            when {
                pdfState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                pdfState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ошибка: ${pdfState.error}",
                            color = Color.White
                        )
                    }
                }

                else -> {
                    PdfPageViewer(
                        pdfState = pdfState,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun rememberPdfState(file: File): PdfState {
    val state = remember { PdfState(file) }

    LaunchedEffect(file) {
        state.loadPdf()
    }

    return state
}

class PdfState(private val file: File) {
    var isLoading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)
    var currentPage by mutableIntStateOf(0)
    var totalPages by mutableIntStateOf(0)
    var scale by mutableFloatStateOf(1f)
    var translationX by mutableFloatStateOf(0f)
    var translationY by mutableFloatStateOf(0f)

    private var pdfRenderer: PdfRenderer? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    suspend fun loadPdf() {
        isLoading = true
        error = null

        try {
            withContext(Dispatchers.IO) {
                parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
                totalPages = pdfRenderer!!.pageCount
            }
        } catch (e: Exception) {
            error = "Не удалось загрузить PDF: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun getCurrentPageBitmap(): Bitmap? {
        return try {
            pdfRenderer?.openPage(currentPage)?.use { page ->
                val bitmap = Bitmap.createBitmap(
                    page.width,
                    page.height,
                    Bitmap.Config.ARGB_8888
                )
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmap
            }
        } catch (e: Exception) {
            null
        }
    }

    fun nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++
            resetZoom()
        }
    }

    fun previousPage() {
        if (currentPage > 0) {
            currentPage--
            resetZoom()
        }
    }

    fun zoomIn() {
        scale = (scale + 0.2f).coerceAtMost(3f)
    }

    fun zoomOut() {
        scale = (scale - 0.2f).coerceAtLeast(0.5f)
    }

    fun resetZoom() {
        scale = 1f
        translationX = 0f
        translationY = 0f
    }

    fun updateTranslation(dx: Float, dy: Float) {
        translationX += dx
        translationY += dy
    }

    fun cleanup() {
        pdfRenderer?.close()
        parcelFileDescriptor?.close()
    }
}

@Composable
fun PdfPageViewer(
    pdfState: PdfState,
    modifier: Modifier = Modifier
) {
    val currentBitmap = remember(pdfState.currentPage) {
        pdfState.getCurrentPageBitmap()
    }

    val transformableState = rememberTransformableState { zoomChange, panChange, rotationChange ->
        pdfState.scale *= zoomChange
        pdfState.updateTranslation(panChange.x, panChange.y)
    }

    Box(
        modifier = modifier
            .transformable(transformableState)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap.asImageBitmap(),
                contentDescription = "Страница ${pdfState.currentPage + 1}",
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = pdfState.scale,
                        scaleY = pdfState.scale,
                        translationX = pdfState.translationX,
                        translationY = pdfState.translationY
                    )
                    .zIndex(1f)
            )
        } else {
            Text("Не удалось загрузить страницу")
        }

        if (pdfState.isLoading) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color.White
                )
            }
        }
    }
}